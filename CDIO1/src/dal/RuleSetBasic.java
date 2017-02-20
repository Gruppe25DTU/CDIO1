package dal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class RuleSetBasic implements IRuleSet {

    private static final String ID_STRING = "id", NAME_STRING = "name"
            , INITIALS_STRING = "initials", ROLES_STRING = "roles"
            , CPR_STRING = "cpr", PWD_STRING = "pwd";
    Map<String, Rule> ruleList = new HashMap<>();

    public RuleSetBasic() {
        createRuleSet();
    }

    private void createRuleSet() {
        int minID = 11, maxID = 99;
        int minName = 2, maxName = 20;
        int minIni = 2, maxIni = 3;
        int minPwd = 6, minPwdReq = 3;
        String[][] exclusiveRoles = new String[][] {{
                "Pharmacist", "Foreman",
        }};
        Rule<Integer> idRule = new Rule<>
                ("ID must be between" + minID + " and " + maxID
                        , t -> t > minID && t < maxID);
        Rule<String> nameRule = new Rule<>
                ("Name must be between " + minName + " and " + maxName + " characters"
                        , t -> t.length() >= minName && t.length() <= maxName);
        Rule<String> iniRule = new Rule<>
                ("Initials must be between " + minIni + " and " + maxIni + " characters"
                        , t -> t.length() >= minIni && t.length() <= maxIni);
        Rule<String> cprRule = new Rule<>
                ("CPR must be entered as 'xxxxxx-yyyy' or 'xxxxxxyyyy'"
                        , t -> Pattern.matches("[0-9]{6}-?[0-9]{4}", t));
        Rule<String> pwdRule = new Rule<>
                ("Password must be at least " + minPwd + " long and contain at least "
                        + minPwdReq + " of these categories:\n" +
                        "* Lowercase letters\n" +
                        "* Uppercase letters\n" +
                        "* Numbers\n" +
                        "* Special characters (Use only\". - _ + ! ? =\")"
                        , t -> {
                    int hasSize = t.length() >= minPwd ? 1 : 0;
                    int hasLowerCase = t.matches(".*[a-zæøå]+.*") ? 1 : 0;
                    int hasUpper = t.matches(".*[A-ZÆØÅ]+.*") ? 1 : 0;
                    int hasNumber = t.matches(".*[0-9]+.*") ? 1 : 0;
                    int hasSpecial = t.matches(".*[.-_+!?=]+.*") ? 1 : 0;
                    boolean hasIllegal = !t.matches("[a-zæøåA-ZÆØÅ0-9.-_+!?=]*");

                    return (!hasIllegal && ((hasSize + hasLowerCase + hasUpper + hasNumber + hasSpecial) >= minPwdReq));
                }
                );
        String roleRuleString = "Following roles cannot be assigned at the same time:";
        for (int i = 0; i < exclusiveRoles.length; i++) {
            roleRuleString += "Exclusive Group " + i + "\n";
            for (int j = 0; j < exclusiveRoles[i].length; j++) {
                roleRuleString += "\t\"" + exclusiveRoles[i][j] + "\"\n";
            }
        }
        Rule roleRule = new Rule<Set<String>>(
                roleRuleString
                , t -> {
            for (String[] exclusiveRole : exclusiveRoles) {
                int count = 0;
                for (int j = 0; j < exclusiveRole.length; j++) {
                    if (t.contains(exclusiveRole[j])) {
                        count++;
                    }
                }
                if (count > 1) {
                    return false;
                }
            }
            return true;
        });
        ruleList.put(ID_STRING, idRule);
        ruleList.put(NAME_STRING, nameRule);
        ruleList.put(INITIALS_STRING, iniRule);
        ruleList.put(CPR_STRING, cprRule);
        ruleList.put(PWD_STRING, pwdRule);
        ruleList.put(ROLES_STRING, roleRule);
    }

    @Override
    public Rule getIdReq() {
        return ruleList.get(ID_STRING);
    }

    @Override
    public Rule getNameReq() {
        return ruleList.get(NAME_STRING);
    }

    @Override
    public Rule getIniReq() {
        return ruleList.get(INITIALS_STRING);
    }

    @Override
    public Rule getCprReq() {
        return ruleList.get(CPR_STRING);
    }

    @Override
    public Rule getRoleReq() {
        return ruleList.get(ROLES_STRING);
    }

    @Override
    public Rule getPwdReq() {
        return ruleList.get(PWD_STRING);
    }
}
