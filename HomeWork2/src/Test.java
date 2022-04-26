
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

    public static void main(String[] args) throws Exception {
        ArrayList<Figure> f = new ArrayList<>();
        Checker c1 = new Checker('a', '1', 0);
        Checker c2 = new Checker('b', '2', 0);
        Checker c3 = new Checker('c', '4', 0);
        f.add(c1);
        f.add(c2);
        f.add(c3);
        Checker.print();
    }
}

abstract class A {
    int x;

    A(int x) {
        this.x = x;
    }
    int g() {
        return this.f() + x;
    }
    abstract int f();
}

class B extends A {
    B(int x) {
        super(x);
    }
    @Override
    int f() {
        return 1;
    }
}

class C extends A {
    C(int x) {
        super(x);
    }
    @Override
    int f() {
        return 2;
    }
}
