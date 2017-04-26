package microcontroller;

import components.CodeMemory;
import components.Memory;
import core.Main;
import exceptions.InstructionException;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class Cpu {

    public Cpu(){
        resetCpu();
    }

    public void resetCpu(){
        linePointer=0;
        mainMemory = new Memory();
        bitMap.clear();
        bitMap.put("ACC.0","11100000");
        bitMap.put("ACC.1","11100001");
        bitMap.put("ACC.2","11100010");
        bitMap.put("ACC.3","11100011");
        bitMap.put("ACC.4","11100100");
        bitMap.put("ACC.5","11100101");
        bitMap.put("ACC.6","11100110");
        bitMap.put("ACC.7","11100111");

       /* psw.put("P",true);
        psw.put("OV",false);
        psw.put("RS0",false);
        psw.put("RS1",false);
        psw.put("F0",false);
        psw.put("AC",false);
        psw.put("C",false);
*/

    }

    private void machineCycle(){

    }

    public void executeInstruction() throws InstructionException{
        String toExecute = codeMemory.getFromAddress(linePointer);
        //System.out.println(linePointer + " " + toExecute);
        if(toExecute.equals("00000010")) { //LJMP
            machineCycle();
            machineCycle();
            linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2),2);
        }
        else if(toExecute.substring(0,5).equals("11011")) { //DJNZ Rx,label/adres (offset)
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            wartosc--;
            if(wartosc==-1)
                wartosc=255;

            mainMemory.put(rejestrString,wartosc);

            if(wartosc>0) {
                int wynik = linePointer+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            if(wartosc==0) {
                linePointer+=2;
            }
        } else if(toExecute.equals("11010101")) { //DJNZ direct,offset
            machineCycle();
            machineCycle();
            int wartosc = mainMemory.getDirect(codeMemory.getFromAddress(linePointer+1));
            wartosc--;
            if(wartosc==-1)
                wartosc=255;

            mainMemory.putDirect(codeMemory.getFromAddress(linePointer+1),wartosc);

            if(wartosc>0) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            if(wartosc==0) {
                linePointer+=3;
            }
        }
        else if(toExecute.equals("01010100")) { //ANL a,#01h
            machineCycle();
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1));
            int wynik = wartosc & mainMemory.get("A");
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("11110100")) { //CPL A
            machineCycle();
            int wartosc = mainMemory.get("A");
            wartosc = ~wartosc;
            if(wartosc<0)
                wartosc+=256;
            if(wartosc>255)
                wartosc-=256;
            System.out.println(wartosc);
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("10110011")) { //CPL C
            machineCycle();
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            linePointer+=1;
        }
        else if(toExecute.equals("10110010")) { //CPL Bit
            machineCycle();
            String rejestr = getKeyFromBitMap(codeMemory.getFromAddress(linePointer+1));
            String podzielone[] = rejestr.split("\\.");
            int wartosc = mainMemory.get(podzielone[0]);
            String wartoscString = expandTo8Digits(Integer.toBinaryString(wartosc));
            int index = Integer.parseInt(podzielone[1]);
            index = 7-index;
            String wynik = wartoscString.charAt(index) + "";
            if(wynik.equals("1"))
                wynik = "0";
            else
                wynik = "1";
            if(index==0)
                wartoscString = wynik + wartoscString.substring(1,8);
            else if(index==7)
                wartoscString = wartoscString.substring(0,7) + wynik;
            else {
                wartoscString = wartoscString.substring(0,index) + wynik + wartoscString.substring(index+1,8);
            }
            mainMemory.put("A",Integer.parseInt(wartoscString,2));
            linePointer+=2;


        }
        else if(toExecute.equals("01000000")) { //JC
            machineCycle();
            machineCycle();
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY"))) {
                int wynik = linePointer + 1 + 1 + Integer.parseInt(codeMemory.getFromAddress(linePointer + 1), 2);
                if (wynik > 255)
                    wynik -= 256;
                linePointer = wynik;
            }
            else
                linePointer+=2;
        }
        else if(toExecute.equals("00100000")) { //JB
            machineCycle();
            machineCycle();
            /*String rejestr = getKeyFromBitMap(codeMemory.getFromAddress(linePointer+1));
            String podzielone[] = rejestr.split("\\.");
            int wartosc = mainMemory.get(podzielone[0]);
            String wartoscString = expandTo8Digits(Integer.toBinaryString(wartosc));
            int index = Integer.parseInt(podzielone[1]);
            if(wartoscString.charAt(7-index)=='1') {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
                System.out.println("tak");
            }
            else {
                linePointer+=3;
            }*/
            /*
            if(mainMemory.getBit(codeMemory.getFromAddress(linePointer+1))) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }*/

        }
        else if(toExecute.equals("01110100")) { // MOV A,#01h
            machineCycle();
            mainMemory.put("A",Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11101")) { //MOV A,Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("11100101")) { //MOV A,direct
            machineCycle();
            mainMemory.put("A",mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)));

            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01111")) { //MOV R,#01
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            mainMemory.put(rejestrString,wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11111")) { //MOV R,A
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            mainMemory.put(rejestrString,mainMemory.get("A"));
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("10101")) { //MOV R,direct
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            mainMemory.put(rejestrString,Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            linePointer+=2;
        }
        else if(toExecute.equals("11110101")) { // direct, a
            machineCycle();
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),mainMemory.get("A"));

            linePointer+=2;
        }
        else if(toExecute.equals("10000101")) { //Px,Px
            machineCycle();
            machineCycle();
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2)));
            linePointer+=3;

        }
        else if(toExecute.equals("01110101")) { //Px, #liczba
            machineCycle();
            machineCycle();
            mainMemory.put( Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)  ,Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2));
            linePointer+=3;
        }
        else if(toExecute.equals("00100011")) { //RL A
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            System.out.println(stringWartosc);
            String wynik = "";
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik += tmp;
            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00000011")) { //RR A
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik = tmp + wynik;
            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00110011")) { //RLC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik += stringWartosc.charAt(i);
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik += "1";
            else
                wynik += "0";
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }

        else if(toExecute.equals("00010011")) { // RRC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik += stringWartosc.charAt(i);
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik = "1" + wynik;
            else
                wynik = "0" + wynik;
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00100100")) { //ADD A,#01h
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.equals("00110100")) { //ADDC A,#01h
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.substring(0,5).equals("00101")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("00111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("00100101")) {
            int doDodania = 0;

            doDodania = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));

            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("00110101")) {
            int doDodania = 0;
            doDodania = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("10010100")) { //SUBB A,#01h
            machineCycle();
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            int obecnaWartosc = mainMemory.get("A");
            int doOdjecia = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.substring(0,5).equals("10011")) { //SUBB A,Rx
            machineCycle();
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doOdjecia = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("10010101")) { //SUBB A,Px
            int doOdjecia = 0;
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            doOdjecia = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));

            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("11010011")) { //SETB C
            machineCycle();
            //psw.put("C",true);
            mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            linePointer++;
        }
        else if(toExecute.equals("11010010")) { //SETB BIT
            machineCycle();
            mainMemory.setBit(codeMemory.getFromAddress(linePointer+1),true);
            linePointer+=2;
        }

        else if(toExecute.equals("11000010")) { //CLR BIT
            machineCycle();
            mainMemory.setBit(codeMemory.getFromAddress(linePointer+1),false);
            linePointer+=2;
        }
        else if(toExecute.equals("11100100")) { //CLR A
            machineCycle();
            mainMemory.put("A",0);
            linePointer++;
        }
        else if(toExecute.equals("11000011")) { //CLR C
            machineCycle();
            //psw.put("C",false);
            mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            linePointer++;
        }
        refreshStatusRegister();
        refreshGui();
    }

    private void refreshStatusRegister(){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        if(acc.length()>4)
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),false);
        int jedynek = 0;
        for(int i = 0; i < acc.length();i++) {
            if(acc.charAt(i) == '1')
                jedynek++;
        }
        if(jedynek%2==0)
            mainMemory.setBit(codeMemory.bitAddresses.get("P"),true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("P"),false);
    }
    public void refreshGui(){
        Main.stage.r0TextField.setText(Integer.toHexString(mainMemory.get("R0")).toUpperCase()+"h");
        Main.stage.r1TextField.setText(Integer.toHexString(mainMemory.get("R1")).toUpperCase()+"h");
        Main.stage.r2TextField.setText(Integer.toHexString(mainMemory.get("R2")).toUpperCase()+"h");
        Main.stage.r3TextField.setText(Integer.toHexString(mainMemory.get("R3")).toUpperCase()+"h");
        Main.stage.r4TextField.setText(Integer.toHexString(mainMemory.get("R4")).toUpperCase()+"h");
        Main.stage.r5TextField.setText(Integer.toHexString(mainMemory.get("R5")).toUpperCase()+"h");
        Main.stage.r6TextField.setText(Integer.toHexString(mainMemory.get("R6")).toUpperCase()+"h");
        Main.stage.r7TextField.setText(Integer.toHexString(mainMemory.get("R7")).toUpperCase()+"h");

        Main.stage.p0TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P0"))) + "b");
        Main.stage.p1TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P1"))) + "b");
        Main.stage.p2TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P2"))) + "b");
        Main.stage.p3TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P3"))) + "b");

        Main.stage.accumulatorTextFieldHex.setText(Integer.toHexString(mainMemory.get("A")).toUpperCase()+"h");
        Main.stage.accumulatorTextFieldBin.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("A")).toUpperCase()) + "b");
        Main.stage.accumulatorTextFieldDec.setText(Integer.toString(mainMemory.get("A")).toUpperCase()+"d");
        Main.stage.bTextFieldHex.setText(Integer.toHexString(mainMemory.get("B")).toUpperCase()+"h");
        Main.stage.bTextFieldBin.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("B")).toUpperCase()) + "b");
        Main.stage.bTextFieldDec.setText(Integer.toString(mainMemory.get("B")).toUpperCase()+"d");

        boolean value = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
        if(value) {
            Main.stage.cTextField.setText("1");
            Main.stage.cTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.cTextField.setText("0");
            Main.stage.cTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("AC"));
        if(value) {
            Main.stage.acTextField.setText("1");
            Main.stage.acTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.acTextField.setText("0");
            Main.stage.acTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("FO"));
        if(value) {
            Main.stage.f0TextField.setText("1");
            Main.stage.f0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.f0TextField.setText("0");
            Main.stage.f0TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("RS1"));
        if(value) {
            Main.stage.rs1TextField.setText("1");
            Main.stage.rs1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs1TextField.setText("0");
            Main.stage.rs1TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("RS0"));
        if(value) {
            Main.stage.rs0TextField.setText("1");
            Main.stage.rs0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs0TextField.setText("0");
            Main.stage.rs0TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("OV"));
        if(value) {
            Main.stage.ovTextField.setText("1");
            Main.stage.ovTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ovTextField.setText("0");
            Main.stage.ovTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("P"));
        if(value) {
            Main.stage.pTextField.setText("1");
            Main.stage.pTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.pTextField.setText("0");
            Main.stage.pTextField.setTextFill(Color.RED);
        }

    }

    public static String expandTo8Digits(String number) {
        int howMany = 8 - number.length();
        for(;howMany>0;howMany--) {
            number = "0"+number;
        }
        return number;
    }


    public void resetCounter(){
        linePointer = 0;
    }

    private int linePointer;
    public Memory mainMemory;
    //private Map<String,Boolean> psw = new HashMap<>();
    public CodeMemory codeMemory = new CodeMemory();

    public Map<String,String> bitMap = new HashMap<>();

    public String getKeyFromBitMap(String toFind){
        for(String s:bitMap.keySet()) {
            if(bitMap.get(s).equals(toFind))
                return s;
        }
        return "";
    }
}