package asmgenerator;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by olegstotsky on 09/09/2018.
 */
enum AsmCommandType {
    PUSH, POP, IMUL, DIV, ADD, SUB, NEG, NOT, OR, AND, XOR, SHL, SHR, CALL, MOV, RET, TEST, CMP, JZ, JNZ, JMP, JG, JGE, JL, JLE, JE, JNE, FLD, FILD, FSTP, FADD, FSUB, FDIV, FMUL, FIADD, FISUB, FIDIV, FIMUL, JA, JB, JAE, JBE, FCOM, FCOMI, FCOMIP
}

enum AsmRegisterType {
    EAX, EBC, ECX, EDX, EBP, ESP, ST0, ST1
}

public class AsmGenerator {
    public ArrayList<AsmCommand> commands;
    public PrintStream printStream;

    public AsmGenerator(PrintStream printStream) {
        this.commands = new ArrayList<>();
        this.printStream = printStream;
    }

    public void addCommand(AsmCommandType type, AsmRegisterType registerType) {
        AsmCommand command = new AsmCommandUnary(type, new AsmRegister(registerType));
        commands.add(command);
    }

    public void addCommand(AsmCommandType type, AsmRegisterType registerType, int value) {
        AsmCommand command = new AsmCommandBinary(type, new AsmRegister(registerType), new AsmIntConst(value));
        commands.add(command);
    }

    public void addCommand(AsmCommandType type, AsmRegisterType firstRegisterType, AsmRegisterType secondRegisterType) {
        AsmCommand command = new AsmCommandBinary(type, new AsmRegister(firstRegisterType), new AsmRegister(secondRegisterType));
        commands.add(command);
    }



    public void print() {
        for (AsmCommand command: commands) {
            this.printStream.println(command.getCode());
        }
    }
}

abstract class AsmCommand {
    public AsmCommandType type;

    public AsmCommand(AsmCommandType type) {
        this.type = type;
    }

    abstract String getCode();
}

class AsmCommandUnary extends AsmCommand {
    public AsmOperand operand;

    public AsmCommandUnary(AsmCommandType type, AsmOperand operand) {
        super(type);
        this.operand = operand;
    }

    public String getCode() {
        return this.type.toString() + " " + this.operand.getCode();
    }
}

class AsmCommandBinary extends AsmCommand {
    public AsmOperand operand1;
    public AsmOperand operand2;

    public AsmCommandBinary(AsmCommandType type, AsmOperand operand1, AsmOperand operand2) {
        super(type);
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public String getCode() {
        return this.type.toString() + " " + operand1.getCode() + ", " + operand2.getCode();
    }
}

abstract class AsmOperand {
    public abstract String getCode();
}

class AsmRegister extends AsmOperand {
    public AsmRegisterType type;

    public AsmRegister(AsmRegisterType type) {
        this.type = type;
    }

    public String getCode() {
        return type.toString();
    }
}

class AsmIntConst extends AsmOperand {
    public int value;

    public AsmIntConst(int value) {
        this.value = value;
    }

    public String getCode() {
        return String.valueOf(value);
    }
}

class AsmStringConst extends AsmOperand {
    public String value;

    public AsmStringConst(String value) {
        this.value = value;
    }

    public String getCode() {
        return value;
    }
}

class AsmVar extends AsmOperand {
    public String name;

    public AsmVar(String value) {
        this.name = value;
    }

    public String getCode() {
        return name;
    }
}

