import org.checkerframework.checker.initialization.qual.*;
class Outer {
    class Inner {
        Inner(@UnknownInitialization Outer Outer.this) {
            Outer.this.f.hashCode();
        }

        void testinner(){
            Outer.this.f.hashCode();
        }
    }

    Outer() {
        Inner i = this.new Inner();
        f = "";
    }

//    void foo(@UnknownInitialization Outer o) {
//        o.f.toString();
//    }

    Object f;

    public static void main(String [] args) {
        new Outer();
    }
}
