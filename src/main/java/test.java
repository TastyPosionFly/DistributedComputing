import java.util.ArrayList;

class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;
    public TreeNode(int val) {
     this.val = val;
   }
}

public class test {
    public ArrayList<ArrayList<Integer>> setZero(ArrayList<ArrayList<Integer>> matrix) {
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) == 0) {
                    ArrayList<Integer> temp1 = new ArrayList<>();
                    temp1.add(i);
                    temp1.add(j);
                    temp.add(temp1);
                }
            }
        }

        for (ArrayList<Integer> temp1 : temp) {
            int i = temp1.get(0);
            int j = temp1.get(1);
            for (int k = 0; k < matrix.get(i).size(); k++) {
                matrix.get(i).set(k, 0);
            }
            for (int k = 0; k < matrix.size(); k++) {
                matrix.get(k).set(j, 0);
            }
        }

        return matrix;
    }

    public int lowestCommonAncestor(TreeNode root, int m, int n) {
        TreeNode temp = BFS(root, m, n);
        return temp.val;
    }

    public TreeNode BFS(TreeNode root, int m , int n) {
        if (root == null || root.val == m || root.val == n) {
            return root;
        }

        TreeNode left = BFS(root.left, m, n);
        TreeNode right = BFS(root.right, m, n);
        if (left != null && right != null) {
            return root;
        }
        return (left == null) ? right : left;
    }

    public int maxWine (int[] weights) {
        int max = 0;
        int i = 0, j;
        for (int k = 0; k < weights.length; k++) {
            j = k;
            int sum = 0;
            while (i < weights.length / 2) {
                sum += weights[j];
                j = (j + 2) % weights.length;
                i ++;
            }
            max = Math.max(max, sum);
            i = 0;
        }
        return max;
    }

    public static void main(String[] args) {
        int[] weights = new int[]{1,5,9,4,2};
        test test = new test();
        int maxWine = test.maxWine(weights);
        System.out.println(maxWine);
    }
}
