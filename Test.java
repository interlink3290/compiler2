import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    /*
     * 从文件中读入语句
     * @return
     */
    public static String txt2String(File file) throws IOException
    {
        StringBuilder result =new StringBuilder();
        try {
            BufferedReader br= new BufferedReader(new FileReader(file));
            String s=null;
            while((s=br.readLine())!=null){
                result.append(s);
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result.toString();
    }
    /*
     * 从文件中读入语句
     * @return
     */
    public static char[] preTreatment(char[] sourcefile)
    {
        char []afterfile = new char[10000];
        int index=0;
        if(sourcefile.length!=0)
        {
            for(int i=0;i<sourcefile.length;i++)
            {

                if(sourcefile[i]!='\n'&&sourcefile[i]!='\r'&&sourcefile[i]!='\t')
                {
                    afterfile[index]=sourcefile[i];
                    index++;
                }
                else {
                    i++;
                }
            }
            index++;
            afterfile[index]='\0';
        }
        return String.copyValueOf(afterfile).substring(0,index-1).toCharArray();
    }

    /*
     * 优先关系表
     * @return
     */
    public static int priorityTable(int a, int b) {
        int [][] table={
                {1, -1, -1, -1, 1, 1},
                {1,  1, -1, -1, 1, 1},
                {1,  1,  2,  2, 1, 1},
                {-1,- 1, -1, -1, 0, 2},
                {1,  1,  2,  2, 1, 1},
                {-1, -1,- 1, -1, 2, 0}
        };
        return table[a][b];
    }
    /*
     * 将字符转换为优先关系表中对应的位置
     * @return
     */
    public static int findPlace(char c) {
        int n=-1;
        switch(c) {
            case '+': n = 0; break;
            case '*': n = 1; break;
            case 'i': n = 2; break;
            case '(': n = 3; break;
            case ')': n = 4; break;
            case '#': n = 5; break;
            default : n = -1;
        }
        return n;
    }
    /*
     * 判断一些输入不合法的情况
     * @return
     */
    public static boolean  judge(char a,char b,int stackNow, int stackTop, int x){
        if(b!='#'&&b!='+'&&b!='*'&&b!='('&&b!=')'&&b!='i'){
            System.out.println("E");
            return false;
        }
        if(a=='('&&b=='#'){
            System.out.println("RE\n");
            return false;
        }
        if((a=='+'||a=='*')&& stackNow==stackTop && x==1){
            System.out.println("RE\n");
            return false;
        }

        return true;
    }

    public static void analysisProcess(char []sentence) {
        List<Character> listStack = new ArrayList<>(); //分析栈
        int stackTop = 0; // 栈顶位置
        int stackNow =0; // 指向栈中位置
        int step = 0; // 步骤
        int tokenNow=0; //当前输入符号位置

        listStack.add('#');

        while(true){

            if(listStack.get(stackTop)=='N')
                stackNow=stackTop-1;
            else
                stackNow=stackTop;

            char a = listStack.get(stackNow);
            char b = sentence[tokenNow];


            if(!judge(a,b,stackNow,stackTop,0)){
                break;
            }

            if(priorityTable(findPlace(a),findPlace(b))==-1){
                // 入栈
                listStack.add(b);
                stackTop++;
                tokenNow++;
                System.out.println("I"+b);
            }
            else if(priorityTable(findPlace(a),findPlace(b))==1){
                // 规约
                if(!judge(a,b,stackNow,stackTop,1)){
                    break;
                }

                char c;
                do{
                    c = listStack.get(stackNow);
                    if(listStack.get(stackNow-1)=='N')
                        stackNow=stackNow-2;
                    else
                        stackNow--;
                } while(priorityTable(findPlace(listStack.get(stackNow)),findPlace(c))!=-1);

                stackTop = stackNow+1;

                // 弹栈，把要规约掉的字符弹出去
                int size=listStack.size();
                for(int i=stackNow+1;i<size;i++){
                    listStack.remove(stackNow+1);
                }

                listStack.add('N');
                System.out.println("R");

            }
            else if(priorityTable(findPlace(a),findPlace(b))==0){
                if(listStack.get(stackNow)=='#'){
                    break;
                }
                else{
                    // 入栈
                    listStack.add(b);
                    stackTop++;
                    tokenNow++;
                    System.out.println("I"+b);
                }
            }
            else{
                System.out.println("E");
                break;
            }

        }

    }

    public static void main(String[] args) throws IOException {
        File file=new File(args[0]);
        String source=txt2String(file)+"#";
        char sourcefile[] = source.toCharArray();
        char afterfile[] = preTreatment(sourcefile);
        System.out.println(afterfile);
        // analysisProcess(afterfile);
    }
}
