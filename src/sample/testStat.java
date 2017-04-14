package sample;

/**
 * Created by Karim on 04/04/2017.
 */
public class testStat
{
        public static void main(String[] args){
            Stat s=new Stat();
            double tab[]=s.apporterDonneePeriode(2014,2017);
            int i=0;
            while(i<tab.length){
                System.out.println(tab[i]);
                i++;
            }
        }
}
