package net.nio.protocol.ftp;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2004
 * Time: 9:44:50 AM
 * To change this template use File | Settings | File Templates.
 */
public enum FtpMethod {
    USER, ACCT, ALLO, APPE, ABOR, CWD, CDUP, SMNT, STRU, STOR, STOU, STAT, SYST, SITE,
    REIN, REST, RETR, RNFR, RNTO, RMD, QUIT, PORT, PASS, PASV, PWD, MODE, MKD, TYPE, LIST,
    NLST, NOOP, DELE, HELP;

    public static FtpMethod parse(String text){
        int l = text.length();
        if (l > 0){
            switch(text.charAt(0)){
                case 'U': return USER;
                case 'A':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'C': return ACCT;
                            case 'L': return ALLO;
                            case 'P': return APPE;
                            case 'B': return ABOR;
                        }
                    }
                    return null;
                case 'C':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'W': return CWD;
                            case 'D': return CDUP;
                        }
                    }
                    return null;
                case 'S':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'M': return SMNT;
                            case 'T':
                                if (l > 2){
                                    switch(text.charAt(2)){
                                        case 'R': return STRU;
                                        case 'O':
                                            switch(text.charAt(3)){
                                                case 'R': return STOR;
                                                case 'U': return STOU;
                                            }
                                            return null;
                                        case 'A': return STAT;
                                    }
                                }
                                return null;
                            case 'Y': return SYST;
                            case 'I': return SITE;
                        }
                    }
                    return null;
                case 'R':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'E':
                                if (l > 2){
                                    switch(text.charAt(2)){
                                        case 'I': return REIN;
                                        case 'S': return REST;
                                        case 'T': return RETR;
                                    }
                                }
                                return null;
                            case 'N':
                                if (l > 2){
                                    switch(text.charAt(2)){
                                        case 'F': return RNFR;
                                        case 'T': return RNTO;
                                    }
                                }
                                return null;
                            case 'M': return RMD;
                        }
                    }
                    return null;
                case 'Q': return QUIT;
                case 'P':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'O': return PORT;
                            case 'A':
                                if (l > 3){
                                    switch(text.charAt(3)){
                                        case 'S': return PASS;
                                        case 'V': return PASV;
                                    }
                                }
                                return null;
                            case 'W': return PWD;
                        }
                    }
                    return null;
                case 'M':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'O': return MODE;
                            case 'K': return MKD;
                        }
                    }
                    return null;
                case 'T': return TYPE;
                case 'L': return LIST;
                case 'N':
                    if (l > 1){
                        switch(text.charAt(1)){
                            case 'L': return NLST;
                            case 'O': return NOOP;
                        }
                    }
                    return null;
                case 'D': return DELE;
                case 'H': return HELP;
            }
        }
        return null;
    }
}
