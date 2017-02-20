package dal;

import java.util.function.Predicate;

/**
 * Created by ymuslu on 20-02-2017.
 */
public interface IRuleSet {

    Rule getIdReq();
    Rule getNameReq();
    Rule getIniReq();
    Rule getCprReq();
    Rule getRoleReq();
    Rule getPwdReq();

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
