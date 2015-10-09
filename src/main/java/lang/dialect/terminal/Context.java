package lang.dialect.terminal;

import lang.dialect.DialectException;
import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:27:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Context extends Expression {
    private Thread creator = Thread.currentThread();
    private boolean mutable = true;
    private Context prev = null;
    protected Map<String, Expression> bindings = null;

    public static final Context ENVIRONMENT = new Context();

    public Context() {
        this(false, null);
    }

    public Context(boolean mutable, Context prev){
        this.mutable = mutable;
        this.prev = prev;
        this.bindings = new HashMap<String, Expression>();
    }

    public Context(Map<String, Expression> bindings){
        this.mutable = false;
        this.bindings = bindings;
    }

    public Context getPrevious(){
        return prev;
    }

    public boolean isMutable() {
        return mutable;
    }

    public boolean isBound(Symbol key) throws DialectException {
        return bindings.get(key.stringValue()) != null;
    }

    public void poke(String key, Expression value) {
        if (Thread.currentThread() != creator) throw new ConcurrentModificationException("context modified by invalid thread");
        bindings.put(key, value);
    }

    public Expression peek(String key){
        return bindings.get(key);
    }

    public void bind(Symbol key, Expression value) throws DialectException {
        if (!mutable) throw new DialectException("cannot modify literal");
        if (Thread.currentThread() != creator) throw new DialectException("context modified by invalid thread");
        if (value == null) value = this;
        bindings.put(key.stringValue(), value);
    }

    public Expression lookup(Symbol key) throws DialectException {
        Expression value = bindings.get(key.stringValue());
        if (value == null) return Literal.UNDEFINED;
        if (value == this) return null;
        return value;
    }

    public Set<Map.Entry<String, Expression>> entrySet(){
        return bindings.entrySet();
    }

    public boolean isContext() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('#');
        writer.write('[');
        boolean comma = false;
        for (Map.Entry<String, Expression> e : bindings.entrySet()){
            if (e.getValue() != null && !e.getValue().isPrimitive()){
                if (comma) writer.write(',');
                writer.write(e.getKey());
                writer.write('=');
                e.getValue().unparse(writer, false);
                comma = true;
            }
        }
        writer.write(']');
        if (statement) writer.write(';');
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Context)) return false;
        Context context = (Context)obj;
        if (bindings.size() != context.bindings.size()) return false;
        Iterator<Map.Entry<String, Expression>> i = bindings.entrySet().iterator();
        Iterator<Map.Entry<String, Expression>> j = context.bindings.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry<String, Expression> x = i.next();
            Map.Entry<String, Expression> y = j.next();
            if (!(x.getKey().equals(y.getKey()) && x.getValue().equals(y.getValue()))) return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        for (Map.Entry<String, Expression> e : bindings.entrySet()){
            result = result * 37 + e.getKey().hashCode();
            result = result * 37 + e.getValue().hashCode();
        }
        return result;
    }

    public static String getString(Context context, String path, String stringDefault) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context == null || context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) return stringDefault;
            context = context.peek(selectors[i]).toContext();
        }
        return (context == null || context.peek(selectors[selectors.length - 1]) == null || !context.peek(selectors[selectors.length - 1]).isTextual()) ? stringDefault : context.peek(selectors[selectors.length - 1]).stringValue();
    }

    public static void setString(Context context, String path, String stringValue) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) context.poke(selectors[i], new Context());
            context = context.peek(selectors[i]).toContext();
        }
        context.poke(selectors[selectors.length - 1], new Textual(stringValue));
    }

    public static boolean getBoolean(Context context, String path, boolean booleanDefault) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context == null || context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) return booleanDefault;
            context = context.peek(selectors[i]).toContext();
        }
        return (context == null || context.peek(selectors[selectors.length - 1]) == null || !context.peek(selectors[selectors.length - 1]).isLogical()) ? booleanDefault : context.peek(selectors[selectors.length - 1]).toLogical().booleanValue();
    }


    public static void setBoolean(Context context, String path, boolean booleanValue) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) context.poke(selectors[i], new Context());
            context = context.peek(selectors[i]).toContext();
        }
        context.poke(selectors[selectors.length - 1], booleanValue ? Logical.TRUE : Logical.FALSE);
    }

    public static long getLong(Context context, String path, long longDefault) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context == null || context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) return longDefault;
            context = context.peek(selectors[i]).toContext();
        }
        return (context == null || context.peek(selectors[selectors.length - 1]) == null || !context.peek(selectors[selectors.length - 1]).isNumeral()) ? longDefault : context.peek(selectors[selectors.length - 1]).toNumeral().longValue();
    }

    public static void setLong(Context context, String path, long longValue) throws DialectException {
        String[] selectors = path.split("/");
        for (int i = 0; i < selectors.length - 1; i++){
            if (context.peek(selectors[i]) == null || !context.peek(selectors[i]).isContext()) context.poke(selectors[i], new Context());
            context = context.peek(selectors[i]).toContext();
        }
        context.poke(selectors[selectors.length - 1], new Numeral(longValue));
    }
}
