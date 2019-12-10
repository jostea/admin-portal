package com.internship.adminpanel.service;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.task.CodeTaskDTOFromUI;
import com.internship.adminpanel.repository.CodeTaskRepository;
import com.internship.adminpanel.repository.CorrectCodeRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class CodeTaskService {
    private final CodeTaskRepository codeTaskRepository;
    private final StreamRepository streamRepository;
    private final CorrectCodeRepository correctCodeRepository;

    public void saveTask(CodeTaskDTOFromUI codeTaskDTOFromUI) throws Exception {
        try {
            if (validateSignature(codeTaskDTOFromUI.getSignature()).equals(codeTaskDTOFromUI.getSignature().replaceAll(" ", ""))) {
                CodeTask codeTask = new CodeTask(codeTaskDTOFromUI);
                List<Stream> codeTaskStreams = new ArrayList<>();
                for (Long s: codeTaskDTOFromUI.getStreams()) {
                    codeTaskStreams.add(streamRepository.findById(s).get());
                }
                codeTask.setStreams(codeTaskStreams);
                codeTaskRepository.save(codeTask);
                for (int i = 0; i < codeTaskDTOFromUI.getCorrectCodes().size(); i++) {
                    codeTaskDTOFromUI.getCorrectCodes().get(i).setCodeTask(codeTask);
                    correctCodeRepository.save(codeTaskDTOFromUI.getCorrectCodes().get(i));
                }
            } else throw new Exception("method validation not passed");
        } catch (Exception e) {
            throw new Exception("Task could not be saved, check all the data again");
        }
    }

    private static List<String> parse(String str) {
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
    private static String cleanSignature(String signature) {
        signature = signature.replaceAll("( )+", " ");
        signature = signature.replaceAll("\\s*,\\s*", ",");
        signature = signature.replaceAll("\\s*,\\s*", ",");
        signature = signature.replaceAll("\\s*\\(\\s*", "(");
        signature = signature.replaceAll("\\s*\\)\\s*", ")");
        signature = signature.replaceAll("\\s*>", ">");
        signature = signature.replaceAll("\\s*<\\s*", "<");
        return signature;
    }
    private static boolean validateDoubleGenerics(String generic) {
        List<String> params = Arrays.asList("Map", "HashMap");
        String cont = "";
        String key = "";
        String value = "";
        if (!params.contains(generic.split("<")[0])) {
            return false;
        } else {
            for (int i = generic.split("<")[0].length()+1; i < generic.length()-1; i++) {
                cont+=generic.split("")[i];
            }
            key = cont.split(",")[0];
            value = cont.split(",")[1];
            if (returnTypes(key) && returnTypes(value) && cont.split(",").length == 2) {
                return true;
            }
        }
        return false;
    }
    private static String getTypeForSimpleGenerics(String generic) {
        List<String> genericSplitList = new LinkedList<String>(Arrays.asList(generic.split("")));
        String finalString = "";
        boolean concat = false;
        for (int i=0; i<genericSplitList.size(); i++) {
            if (genericSplitList.get(i).equals("<") && !concat) {
                concat = true;
                genericSplitList.remove(genericSplitList.size()-1);
                genericSplitList.remove(i);
            }
            if (concat) {
                finalString+=genericSplitList.get(i);
            }
        }
        return finalString;
    }
    private static boolean simpleGeneric(String generic) {
        List<String> params = Arrays.asList("List", "ArrayList", "Set", "HashSet", "Collection", "TreeSet");
        return params.contains(generic);
    }
    private static boolean validateSimpleGenerics(String generic) {
        String holder = generic.split("<")[0];
        String type = getTypeForSimpleGenerics(generic);
        if (!type.isEmpty()) {
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
    private static boolean validateArray(String array) {
        array = array.replaceAll("\\s*\\[\\s*", "[");
        boolean isValid = true;
        if (returnTypes(array.split("\\[")[0])) {
            String brackets = array.split(array.split("\\[")[0])[1];
            for (int i = 0; i < brackets.length(); i+=2) {
                if (!(brackets.split("")[i].equals("[") && brackets.split("")[i+1].equals("]"))) {
                    isValid = false;
                }
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
    private static boolean returnTypes(String ret) {
        List<String> rets = Arrays.asList("int", "double", "char", "boolean", "float", "long", "short", "byte", "Integer", "String",
                "Double", "Char", "Float", "Long", "Short", "Byte", "Object", "Number");
        return rets.contains(ret) || validateSimpleGenerics(ret) || validateArray(ret) || validateDoubleGenerics(ret);
    }
    private static boolean validateMethodName(String name) {
        return (!name.equals("main") && !Character.isDigit(name.charAt(0)));
    }
    private static boolean threeArgsValidation(List<String> list) {
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
    private static boolean fourArgsValidation(List<String> list) {
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
    private static boolean validateArgsName(String name) {
        return !Character.isDigit(name.charAt(0));
    }
    private static boolean noRepetitionTest(List<String> args) {
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
    private static boolean validateArgs(String args) {
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
    private static String validateSignature(String signature) {
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
                } else if (fourArgsValidation(signatureList)) {
                    accessModifier = signatureList.get(0);
                    keyword = signatureList.get(1);
                    returnType = signatureList.get(2);
                    methodName = signatureList.get(3);
                }
            }
        }
        return accessModifier + keyword + returnType + methodName;
    }
}
