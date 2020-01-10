package com.internship.adminpanel.service;

import com.internship.adminpanel.model.dto.code_task.AnswersSubmitDTO;
import com.internship.adminpanel.model.dto.code_task.InputsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class CodeValidationService {

    public List<String> cubeParse(String str) {
        List<String> elements = new ArrayList<>();
        String item = "";
        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            String c = str.split("")[i];
            if (c.equals(",") && depth==0) {
                if (!item.isEmpty()) {
                    elements.add(item);
                    item="";
                }
            } else {
                item+=c;
                if (c.equals("[") || c.equals("{")) {
                    depth++;
                }
                if (c.equals("]") || c.equals("}")) {
                    depth--;
                }
            }
        }
        if (!item.isEmpty()) {
            elements.add(item);
        }
        return elements;
    }

    public List<String> parse(String str) {
        List<String> elements = new ArrayList<>();
        String item = "";
        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            String c = str.split("")[i];
            if (c.equals(",") && depth==0) {
                if (!item.isEmpty()) {
                    elements.add(item);
                    item="";
                }
            } else {
                item+=c;
                if (c.equals("<")) {
                    depth++;
                }
                if (c.equals(">")) {
                    depth--;
                }
            }
        }
        if (!item.isEmpty()) {
            elements.add(item);
        }
        return elements;
    }
    public String cleanSignature(String signature) {
        int cleanBack = Arrays.asList(signature.split("")).indexOf(")")+1;
        signature = signature.substring(0, cleanBack);
        signature = signature.replaceAll("( )+", " ");
        signature = signature.replaceAll("\\s*,\\s*", ",");
        signature = signature.replaceAll("\\s*,\\s*", ",");
        signature = signature.replaceAll("\\s*\\(\\s*", "(");
        signature = signature.replaceAll("\\s*\\)\\s*", ")");
        signature = signature.replaceAll("\\s*>", ">");
        signature = signature.replaceAll("\\s*<\\s*", "<");
        return signature;
    }
    public boolean validateDoubleGenerics(String generic) {
        try {
            List<String> params = Arrays.asList("Map", "HashMap");
            StringBuilder cont = new StringBuilder();
            String key;
            String value;
            if (!params.contains(generic.split("<")[0]) || !Arrays.asList(generic.split("")).contains("<")) {
                return false;
            } else {
                for (int i = generic.split("<")[0].length()+1; i < generic.length()-1; i++) {
                    cont.append(generic.split("")[i]);
                }
                key = parse(cont.toString()).get(0);
                value = parse(cont.toString()).get(1);
                if (returnTypes(key) && returnTypes(value) && parse(cont.toString()).size() == 2) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public String getTypeForSimpleGenerics(String generic) {
        List<String> genericSplitList = new LinkedList<String>(Arrays.asList(generic.split("")));
        StringBuilder finalString = new StringBuilder();
        boolean concat = false;
        for (int i=0; i<genericSplitList.size(); i++) {
            if (genericSplitList.get(i).equals("<") && !concat) {
                concat = true;
                genericSplitList.remove(genericSplitList.size()-1);
                genericSplitList.remove(i);
            }
            if (concat) {
                finalString.append(genericSplitList.get(i));
            }
        }
        return finalString.toString();
    }
    public boolean simpleGeneric(String generic) {
        List<String> params = Arrays.asList("List", "ArrayList", "Set", "HashSet", "Collection", "TreeSet");
        return params.contains(generic);
    }
    public boolean validateSimpleGenerics(String generic) {
        String holder = generic.split("<")[0];
        String type = getTypeForSimpleGenerics(generic);
        if (!type.isEmpty() && returnTypes(type)) {
            if (!(generic.length()==1 && returnTypes(generic))) {
                if (!simpleGeneric(holder)) {
                    return false;
                } else {
                    if (returnTypes(type)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean validateArray(String array) {
        array = array.replaceAll("\\s*\\[\\s*", "[");
        boolean isValid = true;
        if (Arrays.asList(array.split("")).contains("[") && Arrays.asList(array.split("")).contains("]")) {
            if (returnTypes(array.split("\\[")[0])) {
                String brackets = array.split(array.split("\\[")[0])[1];
                for (int i = 0; i < brackets.length(); i+=2) {
                    if (!(brackets.split("")[i].equals("[") && brackets.split("")[i+1].equals("]"))) {
                        isValid = false;
                    }
                }
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
    public boolean returnTypes(String ret) {
        List<String> rets = Arrays.asList("int", "double", "char", "boolean", "float", "long", "short", "byte", "Integer", "String",
                "Double", "Char", "Float", "Long", "Short", "Byte", "Object", "Number");
        if (rets.contains(ret)) {
            return true;
        }
        if (validateSimpleGenerics(ret)){
            return true;
        }
        if (validateArray(ret)) {
            return true;
        }
        if (validateDoubleGenerics(ret)) {
            return true;
        }
        return false;
    }
    public boolean validateMethodName(String name) {
        return (!name.equals("main") && !Character.isDigit(name.charAt(0)));
    }
    public boolean threeArgsValidation(List<String> list) {
        if (list.size()==3) {
            if (list.get(0).equals("static")) {
                if (returnTypes(list.get(1))) {
                    if (validateMethodName(list.get(2))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean fourArgsValidation(List<String> list) {
        if (list.size()==4) {
            if (list.get(0).equals("public")) {
                if (list.get(1).equals("static")) {
                    if (returnTypes(list.get(2))) {
                        if (validateMethodName(list.get(3))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean validateArgsName(String name) {
        return !Character.isDigit(name.charAt(0));
    }
    public boolean noRepetitionTest(List<String> args) {
        List<String> checked = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            if (!checked.contains(args.get(i).split(" ")[1])) {
                checked.add(args.get(i).split(" ")[1]);
            } else {
                return false;
            }
        }
        return true;
    }
    public boolean validateArgs(String args) {
        boolean retval = true;
        if (args.split(" ").length-1==parse(args).size()) {
            for (int i = 0; i < parse(args).size(); i++) {
                if (!(returnTypes(parse(args).get(i).split(" ")[0]) && validateArgsName(parse(args).get(i).split(" ")[1]))) {
                    retval = false;
                }
            }
        } else {
            retval=false;
        }
        if (!noRepetitionTest(parse(args))) {
            retval = false;
        }
        return retval;
    }
    public boolean validateSignature(String signature) {
        String accessModifier = "";
        String returnType = "";
        String keyword = "";
        String methodName = "";
        if (signature.split("\\(").length>1 && signature.split(" ").length>3) {
            signature = cleanSignature(signature);
            String separatedArgs = signature.split("\\(")[1].split("\\)")[0];
            signature = signature.split("\\(")[0];
            List<String> signatureList = Arrays.asList(signature.split(" "));
            if (validateArgs(separatedArgs)) {
                if (threeArgsValidation(signatureList)) {
                    keyword = signatureList.get(0);
                    returnType = signatureList.get(1);
                    methodName = signatureList.get(2);
                    return true;
                } else if (fourArgsValidation(signatureList)) {
                    accessModifier = signatureList.get(0);
                    keyword = signatureList.get(1);
                    returnType = signatureList.get(2);
                    methodName = signatureList.get(3);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validateSimpleAnswers(InputsDTO inputsDTO) {
        List<String> ints = Arrays.asList("Integer", "Double", "Float", "Long", "Short", "int", "double", "long", "short", "float");
        if (inputsDTO.getType().equals(String.class.getSimpleName()) && !inputsDTO.getValue().isEmpty()) {
            return true;
        } else if (ints.contains(inputsDTO.getType())) {
            if (inputsDTO.getValue() == null) {
                return false;
            }
            try {
                double d = Double.parseDouble(inputsDTO.getValue());
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        } else if (inputsDTO.getType().equals(Boolean.class.getSimpleName()) || inputsDTO.getType().equals("boolean")) {
            return inputsDTO.getValue().equals("true") || inputsDTO.getValue().equals("false");
        } else if (inputsDTO.getType().equals(Character.class.getSimpleName()) || inputsDTO.getType().equals("char")) {
            return inputsDTO.getValue().getBytes().length == 1;
        } else if (inputsDTO.getType().equals(Byte.class.getSimpleName()) || inputsDTO.getType().equals("byte")) {
            return true;
        }
        return false;
    }

    public boolean validateSimpleGenericsAnswers(InputsDTO inputsDTO) {
        List<String> generics = Arrays.asList("List", "ArrayList", "Set", "HashSet", "Collection", "TreeSet");
        if (generics.contains(inputsDTO.getType().split("<")[0])) {
            if (inputsDTO.getValue().split("")[0].equals("[") && inputsDTO.getValue().split("")[inputsDTO.getValue().length()-1].equals("]")) {
                String plain = inputsDTO.getValue().substring(1, inputsDTO.getValue().length()-1);
                for (String local : cubeParse(plain)) {
                    InputsDTO loopInput = new InputsDTO();
                    loopInput.setType(getTypeForSimpleGenerics(inputsDTO.getType()));
                    loopInput.setValue(local);
                    if (!validateSimpleAnswers(loopInput)) {
                        if(!validateSimpleGenericsAnswers(loopInput)){
                            if (!validateDoubleGenericsAnswers(loopInput)) {
                                if (!validateArrayAnswers(loopInput)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean validateDoubleGenericsAnswers(InputsDTO inputsDTO) {
        String key;
        String value;
        StringBuilder cont = new StringBuilder();
        if (validateDoubleGenerics(inputsDTO.getType())) {
            if (inputsDTO.getValue().split("")[0].equals("{") && inputsDTO.getValue().split("")[inputsDTO.getValue().split("").length-1].equals("}")) {
                String val = inputsDTO.getValue().substring(1, inputsDTO.getValue().length()-1);
                for (int i = inputsDTO.getType().split("<")[0].length()+1; i < inputsDTO.getType().length()-1; i++) {
                    cont.append(inputsDTO.getType().split("")[i]);
                }
                key = parse(cont.toString()).get(0);
                value = parse(cont.toString()).get(1);
                InputsDTO withKey = new InputsDTO();
                withKey.setType(key);
                withKey.setValue(cubeParse(val).get(0));
                InputsDTO withValue = new InputsDTO();
                withValue.setType(value);
                withValue.setValue(cubeParse(val).get(1));
                return (validateSimpleAnswers(withKey) || validateDoubleGenericsAnswers(withKey) || validateSimpleGenericsAnswers(withKey)) && (validateSimpleAnswers(withValue) || validateDoubleGenericsAnswers(withValue) || validateSimpleGenericsAnswers(withValue));
            }
        }
        return false;
    }

    public boolean validateArrayAnswers(InputsDTO inputsDTO) {
        if (validateArray(inputsDTO.getType())) {
            if (inputsDTO.getValue().split("")[0].equals("[") && inputsDTO.getValue().split("")[inputsDTO.getValue().length()-1].equals("]")) {
                String plain = inputsDTO.getValue().substring(1, inputsDTO.getValue().length()-1);
                for (String local : cubeParse(plain)) {
                    InputsDTO loopInput = new InputsDTO();
                    loopInput.setType(inputsDTO.getType().split("\\[")[0]);
                    loopInput.setValue(local);
                    if (!validateSimpleAnswers(loopInput)) {
                        if(!validateSimpleGenericsAnswers(loopInput)){
                            if (!validateDoubleGenericsAnswers(loopInput)) {
                                if (!validateArrayAnswers(loopInput)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean validateAnswers(AnswersSubmitDTO answersSubmitDTO) {
        if (validateSimpleAnswers(answersSubmitDTO.getOutput()) || validateSimpleGenericsAnswers(answersSubmitDTO.getOutput())){
            for (InputsDTO inputsDTO : answersSubmitDTO.getInput()) {
                if (!validateSimpleAnswers(inputsDTO)) {
                    if (!validateSimpleGenericsAnswers(inputsDTO)) {
                        if (!validateDoubleGenericsAnswers(inputsDTO)) {
                            if (!validateArrayAnswers(inputsDTO)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
