package dal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class RuleSetBasic implements IRuleSet {

    private static final String ID_STRING = "id", NAME_STRING = "name", INITIALS_STRING = "initials", ROLES_STRING = "roles", CPR_STRING = "cpr", PWD_STRING = "pwd";
    @SuppressWarnings("rawtypes")
	Map<String, Rule> ruleList = new HashMap<>();

    public RuleSetBasic() {
        createRuleSet();
    }

    private void createRuleSet() {
        String[][] exclusiveRoles = new String[][]{{
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
                ("Password must between " + minPwd + "-"+maxPwd+" characters long and contain at least "
                        + minPwdReq + " of these categories:\n" +
                        "* Lowercase letters\n" +
                        "* Uppercase letters\n" +
                        "* Numbers\n" +
                        "* Special characters (Use only\". - _ + ! ? =\")"
                        , t -> {
                    int hasLowerCase = t.matches(".*[a-z]+.*") ? 1 : 0;
                    int hasUpper = t.matches(".*[A-Z]+.*") ? 1 : 0;
                    int hasNumber = t.matches(".*[0-9]+.*") ? 1 : 0;
                    int hasSpecial = t.matches(".*[.-_+!?=]+.*") ? 1 : 0;
                    boolean hasIllegal = !t.matches("[a-zA-Z0-9.-_+!?=]*");

                    return (!hasIllegal &&
                    	   ((hasLowerCase + hasUpper + hasNumber + hasSpecial) >= minPwdReq) 
                    	   && t.length()>=minPwd && t.length()<=maxPwd);
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
            for (String[] exclusionGroup : exclusiveRoles) {
                int count = 0;
                for (String anExclusiveRole : exclusionGroup) {
                    if (t.contains(anExclusiveRole)) {
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
    public Rule<Integer> getIdReq() {
        return ruleList.get(ID_STRING);
    }

    @Override
    public Rule<String> getNameReq() {
        return ruleList.get(NAME_STRING);
    }

    @Override
    public Rule<String> getIniReq() {
        return ruleList.get(INITIALS_STRING);
    }

    @Override
    public Rule<String> getCprReq() {
        return ruleList.get(CPR_STRING);
    }

    @Override
    public Rule<Set<String>> getRoleReq() {
        return ruleList.get(ROLES_STRING);
    }

    @Override
    public Rule<String> getPwdReq() {
        return ruleList.get(PWD_STRING);
    }
}
