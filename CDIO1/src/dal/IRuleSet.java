package dal;

import java.util.Set;
import java.util.function.Predicate;

public interface IRuleSet {
	static final int minID = 11, maxID = 99;
    static final int minName = 2, maxName = 20;
    static final int minIni = 2, maxIni = 4;
    static final int minPwd = 6, minPwdReq = 3;

    Rule<Integer> getIdReq();

    Rule<String> getNameReq();

    Rule<String> getIniReq();

    Rule<String> getCprReq();

    Rule<Set<String>> getRoleReq();

    Rule<String> getPwdReq();

    class Rule<T> {
        String text;
        Predicate<T> pred;

        public Rule(String text, Predicate<T> pred) {
            this.text = text;
            this.pred = pred;
        }

        public boolean test(T t) {
            return pred.test(t);
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
