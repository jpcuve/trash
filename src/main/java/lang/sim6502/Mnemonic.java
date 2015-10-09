package lang.sim6502;

import java.util.EnumSet;

import static lang.sim6502.Mode.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 12, 2004
 * Time: 8:32:20 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Mnemonic {
    ADC(0x61, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), AND(0x21, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), ASL(0x02, EnumSet.of(ABS, ZP, ZPX, ABSX, ACC)),
    BCC(0x90, EnumSet.of(REL)), BCS(0xB0, EnumSet.of(REL)), BEQ(0xF0, EnumSet.of(REL)), BIT(0x20, EnumSet.of(ABS, ZP)), BMI(0x30, EnumSet.of(REL)), BNE(0xD0, EnumSet.of(REL)), BPL(0x10, EnumSet.of(REL)), BRK(0x00, EnumSet.of(IMP)), BVC(0x50, EnumSet.of(REL)), BVS(0x70, EnumSet.of(REL)),
    CLC(0x18, EnumSet.of(IMP)), CLD(0xD8, EnumSet.of(IMP)), CLI(0x58, EnumSet.of(IMP)), CLV(0xB8, EnumSet.of(IMP)), CMP(0xC1, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), CPX(0xE0, EnumSet.of(IMM1, ABS, ZP)), CPY(0xC0, EnumSet.of(IMM1, ABS, ZP)),
    DEC(0xC2, EnumSet.of(ABS, ZP, ZPX, ABSX)), DEX(0xCA, EnumSet.of(IMP)), DEY(0x88, EnumSet.of(IMP)),
    EOR(0x41, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)),
    INC(0xE8, EnumSet.of(ABS, ZP, ZPX, ABSX)), INX(0xE8, EnumSet.of(IMP)), INY(0xC8, EnumSet.of(IMP)),
    JMP(0x40, EnumSet.of(ABS, ABSI)), JSR(0x14, EnumSet.of(ABS)),
    LDA(0xA1, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), LDX(0xA2, EnumSet.of(IMM1, ABS, ZP, ABSY2, ZPY)), LDY(0xA0, EnumSet.of(IMM1, ABS, ZP, ABSX, ZPX)), LSR(0x42, EnumSet.of(ABS, ZP, ZPX, ABSX, ACC)),
    NOP(0xEA, EnumSet.of(IMP)),
    ORA(0x01, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)),
    PHA(0x48, EnumSet.of(IMP)), PHP(0x08, EnumSet.of(IMP)), PLA(0x68, EnumSet.of(IMP)), PLP(0x28, EnumSet.of(IMP)),
    ROL(0x22, EnumSet.of(ABS, ZP, ZPX, ABSX, ACC)), ROR(0x62, EnumSet.of(ABS, ZP, ZPX, ABSX, ACC)), RTI(0x40, EnumSet.of(IMP)), RTS(0x60, EnumSet.of(IMP)),
    SBC(0xE1, EnumSet.of(IMM2, ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), SEC(0x38, EnumSet.of(IMP)), SED(0xF8, EnumSet.of(IMP)), SEI(0x78, EnumSet.of(IMP)), STA(0x81, EnumSet.of(ABS, ZP, ZPXII, ZPIYI, ZPX, ABSX, ABSY)), STX(0x82, EnumSet.of(ABS, ZP, ZPY)), STY(0x80, EnumSet.of(ABS, ZP, ZPX)),
    TAX(0xAA, EnumSet.of(IMP)), TAY(0xA8, EnumSet.of(IMP)), TSX(0xBA, EnumSet.of(IMP)), TXA(0x8A, EnumSet.of(IMP)), TXS(0x9A, EnumSet.of(IMP)), TYA(0x98, EnumSet.of(IMP));

    private final int base;
    private final EnumSet<Mode> modes;

    Mnemonic(int base, EnumSet<Mode> modess){
        this.base = base;
        this.modes = modess;
    }

    public int getBase() {
        return base;
    }

    public EnumSet<Mode> getModes() {
        return modes;
    }

    public int getCode(Mode mode) {
        if (modes.contains(mode)) switch(mode){
            case ZPY:   return base + 0x14;
            case ABSI:  return base + 0x2C;
            case ACC:   return base + 0x08;
            case ABSY:  return base + 0x18;
            case ABSX:  return base + 0x1C;
            case ZPX:   return base + 0x14;
            case ZPIYI: return base + 0x10;
            case ABSY2: return base + 0x1C;
            case ZPXII: return base + 0x00;
            case ZP:    return base + 0x04;
            case ABS:   return base + 0x0C;
            case IMM2:  return base + 0x08;
            case IMM1:  return base + 0x00;
            case REL:   return base + 0x00;
            case IMP:   return base + 0x00;
        }
        return -1;
    }

    public static Mnemonic parse(String string) {
        String s = string.toLowerCase();
        if (s.length() > 0){
            switch(s.charAt(0)){
                case 'a':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'd': return "adc".equals(s) ? ADC : null;
                            case 'n': return "and".equals(s) ? AND : null;
                            case 's': return "asl".equals(s) ? ASL : null;
                        }
                    }
                    return null;
                case 'b':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'c':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'c': return "bcc".equals(s) ? BCC : null;
                                        case 's': return "bcs".equals(s) ? BCS : null;
                                    }
                                }
                                return null;
                            case 'e': return "beq".equals(s) ? BEQ : null;
                            case 'i': return "bit".equals(s) ? BIT : null;
                            case 'm': return "bmi".equals(s) ? BMI : null;
                            case 'n': return "bne".equals(s) ? BNE : null;
                            case 'p': return "bpl".equals(s) ? BPL : null;
                            case 'r': return "brk".equals(s) ? BRK : null;
                            case 'v':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'c': return "bvc".equals(s) ? BVC : null;
                                        case 's': return "bvs".equals(s) ? BVS : null;
                                    }
                                }
                                return null;
                        }
                    }
                    return null;
                case 'c':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'l':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'c': return "clc".equals(s) ? CLC : null;
                                        case 'd': return "cld".equals(s) ? CLD : null;
                                        case 'i': return "cli".equals(s) ? CLI : null;
                                        case 'v': return "clv".equals(s) ? CLV : null;
                                    }
                                }
                                return null;
                            case 'm': return "cmp".equals(s) ? CMP : null;
                            case 'p':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'x': return "cpx".equals(s) ? CPX : null;
                                        case 'y': return "cpy".equals(s) ? CPY : null;
                                    }
                                }
                                return null;
                        }
                    }
                    return null;
                case 'd':
                    if (s.length() > 2 && s.charAt(1) == 'e'){
                        switch(s.charAt(2)){
                            case 'c': return "dec".equals(s) ? DEC : null;
                            case 'x': return "dex".equals(s) ? DEX : null;
                            case 'y': return "dey".equals(s) ? DEY : null;
                        }
                    }
                    return null;
                case 'e':
                    return "eor".equals(s) ? EOR : null;
                case 'i':
                    if (s.length() > 2 && s.charAt(1) == 'n'){
                        switch(s.charAt(2)){
                            case 'c': return "inc".equals(s) ? INC : null;
                            case 'x': return "inx".equals(s) ? INX : null;
                            case 'y': return "iny".equals(s) ? INY : null;
                        }
                    }
                    return null;
                case 'j':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'm': return "jmp".equals(s) ? JMP : null;
                            case 's': return "jsr".equals(s) ? JSR : null;
                        }
                    }
                    return null;
                case 'l':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'd':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'a': return "lda".equals(s) ? LDA : null;
                                        case 'x': return "ldx".equals(s) ? LDX : null;
                                        case 'y': return "ldy".equals(s) ? LDY : null;
                                    }
                                }
                                return null;
                            case 's': return "lsr".equals(s) ? LSR : null;
                        }
                    }
                    return null;
                case 'n':
                    return "nop".equals(s) ? NOP : null;
                case 'o':
                    return "ora".equals(s) ? ORA : null;
                case 'p':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'h':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'a': return "pha".equals(s) ? PHA : null;
                                        case 'p': return "php".equals(s) ? PHP : null;
                                    }
                                }
                                return null;
                            case 'l':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'a': return "pla".equals(s) ? PLA : null;
                                        case 'p': return "plp".equals(s) ? PLP : null;
                                    }
                                }
                                return null;
                        }
                    }
                    return null;
                case 'r':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'o':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'l': return "rol".equals(s) ? ROL : null;
                                        case 'r': return "ror".equals(s) ? ROR : null;
                                    }
                                }
                                return null;
                            case 't':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'i': return "rti".equals(s) ? RTI : null;
                                        case 's': return "rts".equals(s) ? RTS : null;
                                    }
                                }
                                return null;
                        }
                    }
                    return null;
                case 's':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'b': return "sbc".equals(s) ? SBC : null;
                            case 'e':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'c': return "sec".equals(s) ? SEC : null;
                                        case 'd': return "sed".equals(s) ? SED : null;
                                        case 'i': return "sei".equals(s) ? SEI : null;
                                    }
                                }
                                return null;
                            case 't':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'a': return "sta".equals(s) ? STA : null;
                                        case 'x': return "stx".equals(s) ? STX : null;
                                        case 'y': return "sty".equals(s) ? STY : null;
                                    }
                                }
                                return null;
                        }
                    }
                    return null;
                case 't':
                    if (s.length() > 1){
                        switch(s.charAt(1)){
                            case 'a':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'x': return "tax".equals(s) ? TAX : null;
                                        case 'y': return "tay".equals(s) ? TAY : null;
                                    }
                                }
                                return null;
                            case 's': return "tsx".equals(s) ? TSX : null;
                            case 'x':
                                if (s.length() > 2){
                                    switch(s.charAt(2)){
                                        case 'a': return "txa".equals(s) ? TXA : null;
                                        case 's': return "txs".equals(s) ? TXS : null;
                                    }
                                }
                                return null;
                            case 'y': return "tya".equals(s) ? TYA : null;
                        }
                    }
                    return null;
            }
        }
        return null;
    }
}
