package parser.pdf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.PdfException;
import parser.pdf.impl.codec.RC4InputStream;
import parser.pdf.impl.codec.RC4OutputStream;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    private static final Logger LOG = LoggerFactory.getLogger(Context.class);
    private static final byte[] PADDING = { 0x28, 0xBF - 0x100, 0x4E, 0x5E, 0x4E, 0x75, 0x8A - 0x100, 0x41, 0x64, 0x00, 0x4E, 0x56, 0xFF - 0x100, 0xFA - 0x100, 0x01, 0x08, 0x2E, 0x2E, 0x00, 0xB6 - 0x100, 0xD0 - 0x100, 0x68, 0x3E, 0x80 - 0x100, 0x2F, 0x0C, 0xA9 - 0x100, 0xFE - 0x100, 0x64, 0x53, 0x69, 0x7A };
    private static final byte[] METADATAPADDING = { -1, -1, -1, -1 };

    private final File file;
    private final List<XrefTable> xrefs = new ArrayList<XrefTable>();
    private final Map<PdfReference, Object> index = new HashMap<PdfReference, Object>();
    private MessageDigest messageDigest;
    private byte[] key;

    public Context(final File file) {
        this.file = file;
    }

    public void initializeCredentials(final PdfDictionary trailer) throws IOException, NoSuchAlgorithmException, PdfException {
        this.messageDigest = MessageDigest.getInstance("MD5");
        trailer.setContext(this);
        final PdfDictionary encrypt = trailer.getd("Encrypt");
        if (encrypt != null){
            final byte[] password = new byte[0];
            final int rev = encrypt.geti("R");
            final int length = encrypt.containsKey("Length") ? encrypt.geti("Length") / 8 : 5;
            final byte[] o = encrypt.gets("O").getBytes();
            final byte[] u = encrypt.gets("U").getBytes();
            final long p = encrypt.getl("P");
            final byte[] id0 = trailer.geta("ID").gets(0).getBytes();
            final boolean encryptMetadata = encrypt.getb("encryptMetadata");
            // test password as owner password
            byte[] upassword = getUserPasswordFromOwnerPassword(password, rev, o, length);
            byte[] utest = computeU(upassword, rev, o, p, length, id0, encryptMetadata);
            boolean isOwnerPassword = isByteArrayEqual(utest, u, rev >= 3 ? 16 : u.length);
            // test password as user password
            utest = computeU(password, rev, o, p, length, id0, encryptMetadata);
            boolean isUserPassword = isByteArrayEqual(utest, u, rev >= 3 ? 16 : u.length);
            LOG.debug("owner: {}, user: {}", isOwnerPassword, isUserPassword);
            if (isUserPassword) key = computeEncryptionKey(new byte[0], rev, o, p, length, id0, encryptMetadata);
        }
    }

    public void addCrossReferenceTable(final XrefTable xref){
        xrefs.add(xref);
    }

    public File getFile() {
        return file;
    }

    public byte[] getKey() {
        return key;
    }

    public PdfReference getReference(final Object match){
        for (final Map.Entry<PdfReference, Object> entry: index.entrySet()) if (scan(entry.getValue(), match)) return entry.getKey();
        return null;
    }

    private boolean scan(final Object o, final Object match){
        if (o == match) return true;
        if ((o instanceof Container)) for (Object child: ((Container) o).getChildren()) if (scan(child, match)) return true;
        return false;
    }

    public byte[] computeObjectKey(final Object o) throws PdfException {
        if (key == null) return null;
        final PdfReference reference = getReference(o);
        if (reference == null) throw new PdfException("cannot get reference for object: " + o);
        final byte[] okey = new byte[key.length + 5];
        System.arraycopy(key, 0, okey, 0, key.length);
        okey[key.length] = (byte) (reference.getId() & 0xFF);
        okey[key.length + 1] = (byte) ((reference.getId() & 0xFF00) >> 8);
        okey[key.length + 2] = (byte) ((reference.getId() & 0xFF0000) >> 16);
        okey[key.length + 3] = (byte) (reference.getGeneration() & 0xFF);
        okey[key.length + 4] = (byte) ((reference.getGeneration() & 0xFF00) >> 8);
        messageDigest.reset();
        messageDigest.update(okey);
        int l = Math.min(16, key.length + 5);
        final byte[] ret = new byte[l];
        System.arraycopy(messageDigest.digest(), 0, ret, 0, l);
        return ret;
    }

    public byte[] computeEncryptionKey(final byte[] password, int rev, final byte[] o, final long p, final int keyLength, final byte[] id0, final boolean encryptMetadata) {
        messageDigest.reset();
        final byte[] data = new byte[32];
        int size = password.length;
        System.arraycopy(password, 0, data, 0, size > 32 ? 32 : size);
        if (size < 32) System.arraycopy(PADDING, 0, data, size, 32 - size);
        messageDigest.update(data);
        messageDigest.update(o);
        byte[] pbytes = new byte[4];
        pbytes[0] = (byte) (p & 0xFF);
        pbytes[1] = (byte) ((p & 0xFF00) >> 8);
        pbytes[2] = (byte) ((p & 0xFF0000) >> 16);
        pbytes[3] = (byte) ((p & 0xFF000000) >> 24);
        messageDigest.update(pbytes);
        messageDigest.update(id0);
        if (rev >= 4 && !encryptMetadata) messageDigest.update(METADATAPADDING);
        int length = rev >= 3 ? keyLength : 5;
        byte[] digest = new byte[length];
        System.arraycopy(messageDigest.digest(), 0, digest, 0, length);
        if (rev >= 3) for (int i = 0; i < 50; i++) System.arraycopy(messageDigest.digest(digest), 0, digest, 0, length);
        final byte[] key = new byte[length];
        System.arraycopy(digest, 0, key, 0, length);
        return key;
    }

    public byte[] getUserPasswordFromOwnerPassword(byte[] ownerPassword, int rev, byte[] o, int keyLength) throws IOException {
        messageDigest.reset();
        final byte[] data = new byte[32];
        int size = ownerPassword.length;
        System.arraycopy(ownerPassword, 0, data, 0, size > 32 ? 32 : size);
        if (size < 32) System.arraycopy(PADDING, 0, data, size, 32 - size);
        messageDigest.update(data);
        byte[] key = messageDigest.digest();
        if (rev >= 3) for (int i = 0; i < 50; i++) key = messageDigest.digest(key);
        byte[] ret = new byte[0];
        if (rev == 2){
            final byte[] rc4Key = new byte[5];
            System.arraycopy(key, 0, rc4Key, 0, 5);
            ret = RC4InputStream.decode(o, rc4Key);
        } else if (rev >= 3){
            final byte[] rc4Key = new byte[keyLength];
            System.arraycopy(key, 0, rc4Key, 0, keyLength);
            final byte[] nkey = new byte[rc4Key.length];
            ret = new byte[o.length];
            System.arraycopy(o, 0, ret, 0, ret.length);
            for (int i = 19; i >= 0; i--){
                for (int j = 0; j < key.length; j++) nkey[j] = (byte) (key[j] ^ i);
                ret = RC4InputStream.decode(ret, nkey);
            }
        }
        return ret;
    }

    public byte[] computeU(byte[] userPassword, int rev, byte[] o, final long p, final int keyLength, final byte[] id0, final boolean encryptMetadata) throws IOException {
        byte[] key = computeEncryptionKey(userPassword, rev, o, p, keyLength, id0, encryptMetadata);
        byte[] u;
        if (rev >= 3){
            messageDigest.reset();
            messageDigest.update(PADDING);
            messageDigest.update(id0);
            byte[] enc = RC4OutputStream.encode(messageDigest.digest(), key);
            final byte[] nkey = new byte[key.length];
            for (int i = 1; i <= 19; i++) {
                for (int j = 0; j < key.length; j++) nkey[j] = (byte) (key[j] ^ i);
                enc = RC4OutputStream.encode(enc, nkey);
            }
            u = enc;
        } else{
            u = RC4OutputStream.encode(PADDING, key);
        }
        return u;
    }

    private boolean isByteArrayEqual(byte[] b1, byte[] b2, int size){
        if (b1 == null && b2 == null) return true;
        if (b1 == null || b2 == null || (size <= 0 && b1.length != b2.length)) return false;
        for (int i = 0; i < (size > 0 ? size : b1.length); i++) if (b1[i] != b2[i]) return false;
        return true;
    }

    public boolean evalb(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof Boolean ? (Boolean) ret : false;
    }

    public int evali(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof Number ? ((Number) ret).intValue() : 0;
    }

    public long evall(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof Number ? ((Number) ret).longValue() : 0;
    }

    public String evaln(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof String ? (String) ret : null;
    }

    public PdfString evals(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof PdfString ? (PdfString) ret : null;
    }

    public PdfStream evalst(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof PdfStream ? (PdfStream) ret : null;
    }

    public PdfDictionary evald(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof PdfDictionary ? (PdfDictionary) ret : null;
    }

    public PdfArray evala(final Object o) throws PdfException {
        final Object ret = resolve(o);
        return ret instanceof PdfArray ? (PdfArray) ret : null;
    }

    public Object resolve(final Object o) throws PdfException {
        if (o == null) return null;
        Object ret = o;
        while (ret != null && ret instanceof PdfReference){
            final PdfReference ref = (PdfReference) ret;
            ret = index.get(ref);
            if (ret == null) try{
                ret = getObject(ref.getId());
                index.put(ref, ret);
            } catch(IOException x){
                throw new PdfException("cannot resolve reference: " + ref, x);
            }
        }
        if (ret instanceof Container) ((Container) ret).setContext(this);
        return ret;
    }

    private Object getObject(int id) throws IOException {
        for (int i = xrefs.size() - 1; i >= 0; i--){
            final XrefTable xref = xrefs.get(i);
            if (xref.isReferencing(id)){
                final XrefEntry entry = xref.getEntry(id);
                if (entry instanceof XrefFree) throw new IOException("entry is a free object: " + id);
                if (entry instanceof XrefInUse){
                    final XrefInUse xrefInUse = (XrefInUse) entry;
                    final PdfObject o = new Parser(file, xrefInUse.getOffset()).parseObject();
                    if (o == null) throw new IOException("cannot parse object at position: " + xrefInUse.getOffset());
                    return o.getContent();
                }
                throw new IOException("unsupported xref entry type");
            }
        }
        throw new IOException("object id out of range: " + id);
    }

    public void writeTo(final Writer writer) throws IOException {
        for (final Map.Entry<PdfReference, Object> entry: index.entrySet()){
            writer.write(entry.getKey().toString());
            writer.write(':');
            writer.write(entry.getValue().toString());
            writer.write(System.getProperty("line.separator"));
        }
    }
}
