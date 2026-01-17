import java.util.*;

public class main {

    static final int INF = 10000;   //dai dien cho khong co duong
    static final int CITY = 5;      //so thanh pho
    static final int POP = 200;     //kich co quan the
    static final int GEN = 500;     //so the he
    static final double PC = 0.9;   //ti le lai ghep
    static final double PM = 0.02;  //ti le dot bien

    //ma tran khoang cach giua cac thanh pho
    static int[][] dist = {
        {INF, 5, 6, 9, INF},
        {5, INF, 10, 2, 7},
        {6, 10, INF, INF, 15},
        {9, 2, INF, INF, 1},
        {INF, 7, 15, 1, INF}
    };

    static int[][] pop = new int[POP][CITY];    //quan the: moi ca the la mot mang CITY phan tu (hoan vi cua cac thanh pho)
    static double[] fitness = new double[POP];  //mang luu gia tri fitness cua tung ca the
    static Random rd = new Random();   

    // =========================
    // 1. KHỞI TẠO HOÁN VỊ
    // =========================

    //Khoi tao quan the bang cach tao ngau nhien cac hoan vi
    static void init() {
        for (int i = 0; i < POP; i++) {
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < CITY; j++) temp.add(j);
            Collections.shuffle(temp);
            for (int j = 0; j < CITY; j++) pop[i][j] = temp.get(j);
        }
    }

    // =========================
    // 2. FITNESS
    // =========================

    //tinh tong do dai hanh trinh
    static double cost(int[] p) {
        int sum = 0;
        for (int i = 0; i < CITY - 1; i++)
            sum += dist[p[i]][p[i + 1]];
        sum += dist[p[CITY - 1]][p[0]];
        return sum;
    }

    //tinh gia tri fitness cho quan the
    static void evaluate() {
        for (int i = 0; i < POP; i++)
            fitness[i] = 1.0 / cost(pop[i]);
    }

    // =========================
    // 3. ROULETTE SELECTION
    // =========================
    static int[] select() {
        double sum = 0;
        for (double f : fitness) sum += f;

        double r = rd.nextDouble() * sum;
        double cur = 0;

        for (int i = 0; i < POP; i++) {
            cur += fitness[i];
            if (cur >= r) return pop[i].clone();
        }
        return pop[0].clone();
    }

    // =========================
    // 4. ONE-POINT OX CROSSOVER
    // =========================
    static int[] crossover(int[] p1, int[] p2) {
        int k = rd.nextInt(CITY);
        int[] child = new int[CITY];
        Arrays.fill(child, -1);

        for (int i = 0; i < k; i++) child[i] = p1[i];

        int idx = k;
        for (int x : p2)
            if (!contains(child, x))
                child[idx++] = x;

        return child;
    }

    static boolean contains(int[] a, int x) {
        for (int i : a)
            if (i == x) return true;
        return false;
    }

    // =========================
    // 5. SWAP MUTATION
    // =========================
    static void mutate(int[] p) {
        int a = rd.nextInt(CITY);
        int b = rd.nextInt(CITY);
        int t = p[a];
        p[a] = p[b];
        p[b] = t;
    }

    // =========================
    // 6. MAIN LOOP
    // =========================
    public static void main(String[] args) {

        init();

        for (int g = 0; g < GEN; g++) {

            evaluate();
            int[][] newPop = new int[POP][CITY];

            for (int i = 0; i < POP; i++) {

                int[] p1 = select();
                int[] p2 = select();

                int[] child;
                if (rd.nextDouble() < PC)
                    child = crossover(p1, p2);
                else
                    child = p1.clone();

                if (rd.nextDouble() < PM)
                    mutate(child);

                newPop[i] = child;
            }
            pop = newPop;
        }

        evaluate();

        int best = 0;
        for (int i = 1; i < POP; i++)
            if (cost(pop[i]) < cost(pop[best]))
                best = i;

        System.out.println("Best distance = " + cost(pop[best]));
        System.out.println("Best path:");
        for (int x : pop[best])
            System.out.print(x + " ");
    }
}
