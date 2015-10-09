package lang.sim6502;

import lang.sim6502.construct.Block;
import lang.sim6502.construct.Line;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 2, 2005
 * Time: 11:39:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Assembler {
    private Block block;

    public Assembler(Block block) {
        this.block = block;
    }

    public void assemble(){
        for (Expression expression: block.getLines()){
            Line line = (Line)expression;
            // determine opcode and addressing mode
            System.out.println("line=" + line);
            if (line.getOpcode() == null){
                System.out.println(" irrelevant");
            } else{
                Mnemonic mnemonic = Mnemonic.parse(line.getOpcode().toString().toLowerCase());
                /* determine addressing mode
                modes:
                implicit TXA
                accumulator LSR A
                immediate LDA #$10
                zero page LDA $10
                zero page,x STY $10,X
                zero page,y LDY $10,X
                relative BNE *+4
                absolute JMP $1234
                absolute,x STA $3000,X
                absolute,y LDA $4000,Y
                indirect JMP ($1234)
                indexed indirect LDA ($40,X)
                indirect indexed LDA ($40),Y
                */
                if (line.getArgument() == null){
                    System.out.println(" immediate");
                } else {
                }




            }

        }

    }
}
