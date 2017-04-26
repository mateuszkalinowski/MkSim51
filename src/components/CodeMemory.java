package components;

import core.Main;
import exceptions.CompilingException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class CodeMemory {
    public CodeMemory(){
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add("00000000");
        }

        bitAddresses.put("P0.0","10000000");
        bitAddresses.put("P0.1","10000001");
        bitAddresses.put("P0.2","10000010");
        bitAddresses.put("P0.3","10000011");
        bitAddresses.put("P0.4","10000100");
        bitAddresses.put("P0.5","10000101");
        bitAddresses.put("P0.6","10000110");
        bitAddresses.put("P0.7","10000111");

        bitAddresses.put("TCON.0","10001000");
        bitAddresses.put("TCON.1","10001001");
        bitAddresses.put("TCON.2","10001010");
        bitAddresses.put("TCON.3","10001011");
        bitAddresses.put("TCON.4","10001100");
        bitAddresses.put("TCON.5","10001101");
        bitAddresses.put("TCON.6","10001110");
        bitAddresses.put("TCON.7","10001111");

        bitAddresses.put("P1.0","10010000");
        bitAddresses.put("P1.1","10010001");
        bitAddresses.put("P1.2","10010010");
        bitAddresses.put("P1.3","10010011");
        bitAddresses.put("P1.4","10010100");
        bitAddresses.put("P1.5","10010101");
        bitAddresses.put("P1.6","10010110");
        bitAddresses.put("P1.7","10010111");

        bitAddresses.put("SCON.0","10011000");
        bitAddresses.put("SCON.1","10011001");
        bitAddresses.put("SCON.2","10011010");
        bitAddresses.put("SCON.3","10011011");
        bitAddresses.put("SCON.4","10011100");
        bitAddresses.put("SCON.5","10011101");
        bitAddresses.put("SCON.6","10011110");
        bitAddresses.put("SCON.7","10011111");

        bitAddresses.put("P2.0","10100000");
        bitAddresses.put("P2.1","10100001");
        bitAddresses.put("P2.2","10100010");
        bitAddresses.put("P2.3","10100011");
        bitAddresses.put("P2.4","10100100");
        bitAddresses.put("P2.5","10100101");
        bitAddresses.put("P2.6","10100110");
        bitAddresses.put("P2.7","10100111");

        bitAddresses.put("P3.0","10110000");
        bitAddresses.put("P3.1","10110001");
        bitAddresses.put("P3.2","10110010");
        bitAddresses.put("P3.3","10110011");
        bitAddresses.put("P3.4","10110100");
        bitAddresses.put("P3.5","10110101");
        bitAddresses.put("P3.6","10110110");
        bitAddresses.put("P3.7","10110111");

        bitAddresses.put("PSW.0","11010000");
        bitAddresses.put("PSW.1","11010001");
        bitAddresses.put("PSW.2","11010010");
        bitAddresses.put("PSW.3","11010011");
        bitAddresses.put("PSW.4","11010100");
        bitAddresses.put("PSW.5","11010101");
        bitAddresses.put("PSW.6","11010110");
        bitAddresses.put("PSW.7","11010111");

        bitAddresses.put("P","11010000");
        bitAddresses.put("F1","11010001");
        bitAddresses.put("OV","11010010");
        bitAddresses.put("RS0","11010011");
        bitAddresses.put("RS1","11010100");
        bitAddresses.put("FO","11010101");
        bitAddresses.put("AC","11010110");
        bitAddresses.put("CY","11010111");

        bitAddresses.put("ACC.0","11100000");
        bitAddresses.put("ACC.1","11100001");
        bitAddresses.put("ACC.2","11100010");
        bitAddresses.put("ACC.3","11100011");
        bitAddresses.put("ACC.4","11100100");
        bitAddresses.put("ACC.5","11100101");
        bitAddresses.put("ACC.6","11100110");
        bitAddresses.put("ACC.7","11100111");

        bitAddresses.put("B.0","11110000");
        bitAddresses.put("B.1","11100001");
        bitAddresses.put("B.2","11110010");
        bitAddresses.put("B.3","11110011");
        bitAddresses.put("B.4","11110100");
        bitAddresses.put("B.5","11110101");
        bitAddresses.put("B.6","11110110");
        bitAddresses.put("B.7","11110111");



    }
    public String getFromAddress(int line) {
        return emulatedCodeMemory.get(line);
    }

    public int getLineFromLabel(String label) {
        int line = -1;
        for(Pair pair : labels) {
            if(pair.getKey().equals(label)) {
                line = (int)pair.getValue();
                break;
            }
        }
        return line;
    }

    private void show() {
        for(int i = 0; i < 20;i++) {
            //System.out.println(Integer.toHexString(Integer.parseInt(emulatedCodeMemory.get(i),2)));
            System.out.println(emulatedCodeMemory.get(i));
        }
        for(Pair pair : labels) {
            System.out.println(pair.getKey() + " "+pair.getValue());
        }
    }

    public void setMemory(String[] lines) throws CompilingException {
        String newEditorText="";
        int pointer = 0;
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add("00000000");
        }
        labels.clear();
        for(String line : lines) {
                String backupLinii = line;
                int komentarz = -1;
                for(int i = 0; i < line.length();i++) {
                    if(line.charAt(i) == '%') {
                        komentarz = i;
                        break;
                    }
                }
                if(komentarz >0 ) {
                    line = line.substring(0,komentarz);
                }
                if(komentarz == 0) {
                    line = "";
                }
                if(line.length()>0) {

                    line = line.replace(',', ' ');
                    String[] splittedLine = line.split(" ");
                    if(splittedLine.length==1 && splittedLine[0].charAt(splittedLine[0].length()-1)==':') {
                        if(getLineFromLabel(splittedLine[0].toUpperCase().substring(0,splittedLine[0].length()-1))==-1) {
                            try {
                                make8DigitsStringFromNumber(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1));
                                throw new CompilingException("Niepoprawna Etykieta: '" +  splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1));
                            }
                            catch (NumberFormatException e){
                                labels.add(new Pair<>(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1), pointer));
                            }
                        }
                        else
                            throw new CompilingException();
                    }
                    else if(splittedLine[0].equals("ORG") && splittedLine.length==2) {
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='D') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1));
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='H') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),16);
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='B') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),2);
                        }
                    }
                    else {
                        if(splittedLine[0].toUpperCase().equals("LJMP")) {
                            emulatedCodeMemory.set(pointer,"00000010");

                            try {
                                 String destination = make16DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer+1,destination.substring(0,8));
                                emulatedCodeMemory.set(pointer+2,destination.substring(8,16));
                            } catch (Exception e) {
                                emulatedCodeMemory.set(pointer+1,splittedLine[1].toUpperCase());
                            }
                            pointer+=3;
                        }
                        else if(splittedLine[0].toUpperCase().equals("DJNZ")) {

                            String numer = getRAddress(splittedLine[1].toUpperCase());

                            if(numer.equals("")) {
                                try {
                                    String wartosc = make8DigitsStringFromNumber(splittedLine[1]);
                                    emulatedCodeMemory.set(pointer,"11010101");
                                    emulatedCodeMemory.set(pointer+1,wartosc);
                                    emulatedCodeMemory.set(pointer+2,splittedLine[2].toUpperCase());
                                    pointer+=3;
                                }
                                catch (Exception e) {
                                    throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                            }
                            else {
                                emulatedCodeMemory.set(pointer, "11011" + numer);
                                emulatedCodeMemory.set(pointer + 1, splittedLine[2].toUpperCase());
                                pointer+=2;
                            }
                        } else if (splittedLine[0].toUpperCase().equals("ANL")) {
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                   if(splittedLine[2].charAt(0) == '#') {
                                       emulatedCodeMemory.set(pointer,"01010100");
                                       try {
                                           emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                       }
                                       catch (NumberFormatException e) {
                                           throw new CompilingException("Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                       }
                                       pointer+=2;
                                   }
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("CPL")) {
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"11110100");
                                pointer++;
                            }
                            else if(splittedLine[1].toUpperCase().equals("C")) {
                                emulatedCodeMemory.set(pointer,"10110011");
                                pointer++;
                            }
                            else {
                                if(bitAddresses.containsKey(splittedLine[1].toUpperCase())) {
                                    emulatedCodeMemory.set(pointer,"10110010");
                                    emulatedCodeMemory.set(pointer+1,bitAddresses.get(splittedLine[1].toUpperCase()));
                                    pointer+=2;
                                }
                                else {
                                    try {
                                        int numer = Integer.parseInt(make8DigitsStringFromNumber(splittedLine[1]), 2);
                                        emulatedCodeMemory.set(pointer,"10110010");
                                        emulatedCodeMemory.set(pointer+1,make8DigitsStringFromNumber(splittedLine[1]));
                                        pointer+=2;
                                    }
                                    catch (Exception e){
                                        throw new CompilingException("Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }
                                }
                            }


                        }
                        else if(splittedLine[0].toUpperCase().equals("JC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilosć argumentów: '" + backupLinii + "'" );
                            }
                            emulatedCodeMemory.set(pointer,"01000000");
                            emulatedCodeMemory.set(pointer+1,splittedLine[1].toUpperCase());
                            pointer+=2;
                        } else if(splittedLine[0].toUpperCase().equals("JB")) {
                            if(splittedLine.length!=3) {
                                throw new CompilingException("Niepoprawna ilosć argumentów: '" + backupLinii + "'" );
                            }
                            if(bitAddresses.containsKey(splittedLine[1].toUpperCase())) {
                                emulatedCodeMemory.set(pointer,"00100000");
                                emulatedCodeMemory.set(pointer+1,bitAddresses.get(splittedLine[1].toUpperCase()));
                                emulatedCodeMemory.set(pointer+2,splittedLine[2].toUpperCase());
                                pointer+=3;
                            }
                            else {
                                try {
                                    String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                    if(Integer.parseInt(liczba,2)>=0 && Integer.parseInt(liczba,2)<=127)
                                    emulatedCodeMemory.set(pointer,"00100000");
                                    emulatedCodeMemory.set(pointer+1,liczba);
                                    emulatedCodeMemory.set(pointer+2,splittedLine[2].toUpperCase());
                                    pointer+=3;
                                }
                                catch (Exception e) {
                                    throw new CompilingException("Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                            }

                        }
                        else if(splittedLine[0].toUpperCase().equals("MOV")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");

                            if(splittedLine[1].toUpperCase().equals("A")) {
                                if(splittedLine[2].charAt(0)=='#') {
                                    emulatedCodeMemory.set(pointer,"01110100");
                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    }
                                    catch (NumberFormatException e) {
                                        throw new CompilingException("Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='R') {

                                    String numer = getRAddress(splittedLine[2].toUpperCase());

                                    if(numer.equals("")) {
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }

                                    emulatedCodeMemory.set(pointer,"11101" + numer);
                                    pointer+=1;
                                }
                                else {
                                    try {
                                        String address = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                        emulatedCodeMemory.set(pointer,"11100101");
                                        emulatedCodeMemory.set(pointer+1,address);
                                        pointer+=2;
                                    }
                                    catch (Exception e) {
                                        try {
                                            String number = make8DigitsStringFromNumber(splittedLine[2]);
                                            emulatedCodeMemory.set(pointer,"11100101");
                                            emulatedCodeMemory.set(pointer+1,number);
                                            pointer+=2;
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                        }
                                    }
                                }


                            }
                            else if(splittedLine[1].toUpperCase().charAt(0)=='R') {
                                if(splittedLine[2].charAt(0)=='#') {

                                    String numer = getRAddress(splittedLine[1].toUpperCase());

                                    if(numer.equals(""))
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                    emulatedCodeMemory.set(pointer,"01111" + numer);

                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    } catch (NumberFormatException e) {
                                        throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().equals("A")) {

                                    String numer = getRAddress(splittedLine[1].toUpperCase());

                                    if(numer.equals(""))
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                    emulatedCodeMemory.set(pointer,"11111" + numer);
                                    pointer+=1;
                                } else {
                                    String numer = getRAddress(splittedLine[1].toUpperCase());

                                    if(numer.equals(""))
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                    try {
                                        String address = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                        emulatedCodeMemory.set(pointer,"10101" + numer);
                                        emulatedCodeMemory.set(pointer+1,address);
                                        pointer+=2;
                                    }
                                    catch (Exception e) {
                                        try {
                                            String number = make8DigitsStringFromNumber(splittedLine[2]);
                                            emulatedCodeMemory.set(pointer,"10101" + numer);
                                            emulatedCodeMemory.set(pointer+1,number);
                                            pointer+=2;
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        }
                                    }


                                }
                            }  else  {
                                if(splittedLine[2].toUpperCase().equals("A")) {
                                    try {
                                        String address = Main.cpu.mainMemory.get8BitAddress(splittedLine[1].toUpperCase());
                                        emulatedCodeMemory.set(pointer,"11110101");
                                        emulatedCodeMemory.set(pointer+1,address);
                                        pointer+=2;
                                    }
                                    catch (Exception e) {
                                        try {
                                            String number = make8DigitsStringFromNumber(splittedLine[1]);
                                            emulatedCodeMemory.set(pointer,"11110101");
                                            emulatedCodeMemory.set(pointer+1,number);
                                            pointer+=2;
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                        }
                                    }
                                } else if(splittedLine[2].charAt(0)=='#') {
                                    try {
                                        String address = Main.cpu.mainMemory.get8BitAddress(splittedLine[1].toUpperCase());
                                        emulatedCodeMemory.set(pointer,"01110101");
                                        emulatedCodeMemory.set(pointer+1,address);
                                        try {
                                            emulatedCodeMemory.set(pointer+2,make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                            pointer+=3;
                                        }catch (NumberFormatException e) {
                                            throw new CompilingException("Nieznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                        }
                                    }
                                    catch (Exception e) {
                                        try {
                                            String number = make8DigitsStringFromNumber(splittedLine[1]);
                                            emulatedCodeMemory.set(pointer,"01110101");
                                            emulatedCodeMemory.set(pointer+1,number);
                                            try {
                                                emulatedCodeMemory.set(pointer+2,make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                                pointer+=3;
                                            }catch (NumberFormatException e1) {
                                                throw new CompilingException("Nieznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                            }
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                        }
                                    }
                                } else {
                                    String address1 = "";
                                    String address2 = "";

                                    try {
                                        address1 = Main.cpu.mainMemory.get8BitAddress(splittedLine[1].toUpperCase());
                                    }
                                    catch (Exception e) {
                                        try {
                                            address1 = make8DigitsStringFromNumber(splittedLine[1]);
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                                        }
                                    }

                                    try {
                                        address2 = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                    }
                                    catch (Exception e) {
                                        try {
                                            address2 = make8DigitsStringFromNumber(splittedLine[2]);
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        }
                                    }

                                    emulatedCodeMemory.set(pointer,"10000101");
                                    emulatedCodeMemory.set(pointer+1,address1);
                                    emulatedCodeMemory.set(pointer+2,address2);
                                    pointer+=3;

                                }
                            }

                        }
                        else if(splittedLine[0].toUpperCase().equals("RL")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00100011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RLC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00110011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RR")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Nierozpoznana linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00000011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RRC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00010011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("ADD")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("A")) {

                                if(splittedLine[1].toUpperCase().equals("A")) {
                                    if(splittedLine[2].charAt(0)=='#') {
                                        emulatedCodeMemory.set(pointer,"00100100");
                                        try {
                                            emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                        } catch (NumberFormatException e) {
                                            throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        }
                                        pointer+=2;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='R') {

                                        String numer = getRAddress(splittedLine[2].toUpperCase());

                                        if(numer.equals(""))
                                            throw new CompilingException("Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        emulatedCodeMemory.set(pointer,"00101" + numer);
                                        pointer+=1;
                                    }
                                    else {
                                        String address;
                                        try {
                                            address = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                            emulatedCodeMemory.set(pointer,"00100101");
                                            emulatedCodeMemory.set(pointer+1,address);
                                            pointer+=2;
                                        }
                                        catch (Exception e) {
                                            try {
                                                address = make8DigitsStringFromNumber(splittedLine[2]);
                                                emulatedCodeMemory.set(pointer,"00100101");
                                                emulatedCodeMemory.set(pointer+1,address);
                                                pointer+=2;
                                            }
                                            catch (Exception e1) {
                                                throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                                            }
                                        }
                                    }
                                }
                                else
                                    throw new CompilingException("Nierozpoznana etykieta: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("ADDC")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("A")) {

                                if(splittedLine[1].toUpperCase().equals("A")) {
                                    if(splittedLine[2].charAt(0)=='#') {
                                        emulatedCodeMemory.set(pointer,"00110100");
                                        try {
                                            emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                        }
                                        catch (NumberFormatException e) {
                                            throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        }
                                        pointer+=2;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='R') {

                                        String numer = getRAddress(splittedLine[2].toUpperCase());

                                        if(numer.equals(""))
                                            throw new CompilingException("Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        emulatedCodeMemory.set(pointer,"00111" + numer);
                                        pointer+=1;
                                    }
                                    else {
                                        String address;
                                        try {
                                            address = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                            emulatedCodeMemory.set(pointer,"00100101");
                                            emulatedCodeMemory.set(pointer+1,address);
                                            pointer+=2;
                                        }
                                        catch (Exception e) {
                                            try {
                                                address = make8DigitsStringFromNumber(splittedLine[2]);
                                                emulatedCodeMemory.set(pointer,"00100101");
                                                emulatedCodeMemory.set(pointer+1,address);
                                                pointer+=2;
                                            }
                                            catch (Exception e1) {
                                                throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                            }
                                        }
                                    }
                                }
                            }
                            else
                                throw new CompilingException("Nierozpoznana etykieta: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                        }
                        else if(splittedLine[0].toUpperCase().equals("SUBB")) {

                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentów, linia: '" + backupLinii+"'");


                            if(splittedLine[1].toUpperCase().equals("A")) {
                                if(splittedLine[2].charAt(0)=='#') {
                                    emulatedCodeMemory.set(pointer,"10010100");
                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    } catch (NumberFormatException e) {
                                        throw new CompilingException("Nierozpoznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='R') {

                                    String numer = getRAddress(splittedLine[2].toUpperCase());

                                    if(numer.equals(""))
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    emulatedCodeMemory.set(pointer,"10011" + numer);
                                    pointer+=1;
                                }
                                else {
                                    String address;
                                    try {
                                        address = Main.cpu.mainMemory.get8BitAddress(splittedLine[2].toUpperCase());
                                        emulatedCodeMemory.set(pointer,"10010101");
                                        emulatedCodeMemory.set(pointer+1,address);
                                        pointer+=2;
                                    }
                                    catch (Exception e) {
                                        try {
                                            address = make8DigitsStringFromNumber(splittedLine[2]);
                                            emulatedCodeMemory.set(pointer,"10010101");
                                            emulatedCodeMemory.set(pointer+1,address);
                                            pointer+=2;
                                        }
                                        catch (Exception e1) {
                                            throw new CompilingException("Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        }
                                    }
                                }
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("SETB")) {
                            if(splittedLine.length!=2)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("CY") || splittedLine[1].toUpperCase().equals("C")) {
                                emulatedCodeMemory.set(pointer,"11010011");
                                pointer+=1;
                            } else {
                                if(bitAddresses.containsKey(splittedLine[1].toUpperCase())) {
                                    emulatedCodeMemory.set(pointer,"11010010");
                                    emulatedCodeMemory.set(pointer+1,bitAddresses.get(splittedLine[1].toUpperCase()));
                                    pointer+=2;
                                }
                                else {
                                    try {
                                        int numer = Integer.parseInt(make8DigitsStringFromNumber(splittedLine[1]), 2);
                                            emulatedCodeMemory.set(pointer,"11010010");
                                            emulatedCodeMemory.set(pointer+1,make8DigitsStringFromNumber(splittedLine[1]));
                                            pointer+=2;
                                    }
                                    catch (Exception e){
                                        throw new CompilingException("Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }
                                     }
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("CLR")) {
                            if(splittedLine.length!=2)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("CY") || splittedLine[1].toUpperCase().equals("C")) {
                                emulatedCodeMemory.set(pointer,"11000011");
                                pointer+=1;
                            }
                            else if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"11100100");
                            }
                            else {
                                if(bitAddresses.containsKey(splittedLine[1].toUpperCase())) {
                                    emulatedCodeMemory.set(pointer,"11000010");
                                    emulatedCodeMemory.set(pointer+1,bitAddresses.get(splittedLine[1].toUpperCase()));
                                    pointer+=2;
                                }
                                else {
                                    try {
                                        int numer = Integer.parseInt(make8DigitsStringFromNumber(splittedLine[1]), 2);
                                            emulatedCodeMemory.set(pointer,"11000010");
                                            emulatedCodeMemory.set(pointer+1,make8DigitsStringFromNumber(splittedLine[1]));
                                            pointer+=2;
                                    }
                                    catch (Exception e){
                                        throw new CompilingException("Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }
                                }
                            }

                        }
                    }
                }
        }
        for(int i = 0; i < 8192;i++) {
            String s = emulatedCodeMemory.get(i);
            if(s.charAt(s.length()-1)!='0' && s.charAt(s.length()-1)!='1') {
                int numer = getLineFromLabel(s);
                if(numer != -1) {
                    if (emulatedCodeMemory.get(i - 1).equals("00000010")) {
                        emulatedCodeMemory.set(i, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(0, 8));
                        emulatedCodeMemory.set(i + 1, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(8, 16));
                    } else {
                        //offset
                        int wynik = numer - i - 1;
                        if(wynik<0)
                            wynik+=256;
                        if(wynik<0 || wynik>255)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");
                        if(numer>i && wynik>128)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");
                        if(numer<i && wynik<128)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");
                        emulatedCodeMemory.set(i, make8DigitsStringFromNumber(Integer.toString(wynik) + "d"));
                    }
                }
                else {
                    throw new CompilingException("Nieznana Etykieta: '" + s + "'");
                }
            }
        }

        Main.stage.compilationErrorsLabel.setText("Kompilacja przebiegła pomyślnie");
        Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
        //show();
    }

    private ArrayList<String> emulatedCodeMemory;
    private ArrayList<Pair<String,Integer>> labels = new ArrayList<>();



    public String make8DigitsStringFromNumber(String number) throws NumberFormatException {
        String result = "";
            char lastSymbol = number.charAt(number.length()-1);
            if (lastSymbol == 'd' || lastSymbol == 'D')
                number = number.substring(0,number.length()-1);
            int wartosc = -1;
            if (lastSymbol == 'b' || lastSymbol == 'B') {
                try {
                    number = number.substring(0,number.length()-1);
                    wartosc = Integer.parseInt(number, 2);
                }
                catch (Exception e) {
                    throw new NumberFormatException();
                }
            }
            else if(lastSymbol == 'h' || lastSymbol == 'H') {
                try {
                    number = number.substring(0,number.length()-1);
                    wartosc = Integer.parseInt(number, 16);
                }
                catch (Exception e) {
                    throw new NumberFormatException();
                }
            }
            else {
                try {
                    wartosc = Integer.parseInt(number);
                }
                catch (Exception e) {
                    throw new NumberFormatException();
                }
            }
            result = Integer.toBinaryString(wartosc);
            int length = result.length();
            for(int i = length;i<8;i++) {
                result = "0" + result;
            }
            return result;
    }

    public String make16DigitsStringFromNumber(String number) throws CompilingException {
        String result = "";
            char lastSymbol = number.charAt(number.length()-1);
            if (lastSymbol == 'd' || lastSymbol == 'D')
                number = number.substring(0,number.length()-1);
            int wartosc = -1;
            if (lastSymbol == 'b' || lastSymbol == 'B') {
                try {
                    number = number.substring(0,number.length()-1);
                    wartosc = Integer.parseInt(number, 2);
                }
                catch (Exception e) {
                    throw new NumberFormatException();
                }
            }
            else if(lastSymbol == 'h' || lastSymbol == 'H') {
                try {
                    number = number.substring(0,number.length()-1);
                    wartosc = Integer.parseInt(number, 16);
                } catch (Exception e) {
                    throw new NumberFormatException();
                }
            }
            else {
                try {
                    wartosc = Integer.parseInt(number);
                }
                catch (Exception e) {
                    throw new NumberFormatException();
                }
            }

            result = Integer.toBinaryString(wartosc);
            int length = result.length();
            for(int i = length;i<16;i++) {
                result = "0" + result;
            }
            return result;
    }

    private String getRAddress(String label){
        if(label.equals("R0"))
            return "000";
        else if(label.equals("R1"))
            return "001";
        else if(label.equals("R2"))
            return "010";
        else if(label.equals("R3"))
            return "011";
        else if(label.equals("R4"))
            return "100";
        else if(label.equals("R5"))
            return "101";
        else if(label.equals("R6"))
            return "110";
        else if(label.equals("R7"))
            return "111";
        else
            return "";

    }

    public Map<String,String> bitAddresses = new HashMap<>();

}
