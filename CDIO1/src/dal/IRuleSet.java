package dal;

import java.util.Set;
import java.util.function.Predicate;

public interface IRuleSet {

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
