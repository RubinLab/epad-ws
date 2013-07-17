package edu.stanford.isis.tools;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.MySqlQueries;
import edu.stanford.isis.epadws.db.mysql.impl.DbCalls;
import edu.stanford.isis.epadws.db.mysql.impl.DbUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A connection test for DCM4CHEE MySQL database.
 *
 * @author alansnyder
 * Date: 9/14/12
 */
public class DcmDbTest {

    public static final ProxyLogger logger = ProxyLogger.getInstance();

        static final String mysqlConn = "jdbc:mysql://epad-devu.stanford.edu:3306/pacsdb";
        static final String mysqlConnR = "jdbc:mysql://rufus.stanford.edu:3306/pacsdb";

        static final String user = "pacs";
        static final String pw = "pacs";

    public static void main(String[] args){

        try{
            String connectionString = getConnectionStringFromCmdLineArgs(args);
            Connection con = DriverManager.getConnection(connectionString,user,pw);
            doSqlTest1(con);

            MySqlInstance mySqlInstance = MySqlInstance.getInstance();
            mySqlInstance.startup();

            logConnectionStatus();


            doSqlTest2();
            doSqlTest3();
            doSqlTest4();
            doSqlTest5();
            logConnectionStatus();
            doSqlTest6();
            doSqlTest7();
            logConnectionStatus();
            doSqlTest8();



        }catch(Exception e){
            logger.warning("  ",e);
            e.printStackTrace();
        }finally{
            MySqlInstance.getInstance().shutdown();
        }

    }//main

    /**
     * Just test the connection.
     * @param con
     */
    private static void doSqlTest1(Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = con.prepareStatement(DbCalls.SELECT_STUDY_FK_ON_FS_TABLE);
            rs = ps.executeQuery();

            while(rs.next()){
                String studyFk = rs.getString("study_fk");
                String filesystemFk = rs.getString("filesystem_fk");
                Timestamp ts = rs.getTimestamp("access_time");
                logger.info("Study: "+studyFk+" filesystem: "+filesystemFk+" timestamp: "+ts);
            }

        }catch (Exception e){
            logger.warning("doSqlTest1",e);
        }finally{
            DbUtils.close(rs);
            DbUtils.close(ps);
        }
    }

    /**
     * Try to get a list of all the studies using the MySqlInstance facilities.
     */
    private static void doSqlTest2(){
        try{
            logger.info("######## Start test #2 - basic search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results =  mySqlQueries.doStudySearch("patientName","*");

            String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Study Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
                sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
                sb.append("modality=").append(currResult.get(keys[2])).append(",");
                sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
                sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

                resultIndex++;
                //to get keys from a database call.
                //printKeys(currResult.keySet());


            }//for
            logger.info(sb.toString());
            logger.info("######## End test #2 ########");
        }catch (Exception e){
            logger.warning("doSqlTest2 had: ",e);
        }
    }

    /**
     * Do wild-card searches work?
     * @param con
     */
    private static void doSqlTest3(){
        try{
            logger.info("######## Start test #3 - wildcard search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results =  mySqlQueries.doStudySearch("patientName","A*");

            String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Study Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
                sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
                sb.append("modality=").append(currResult.get(keys[2])).append(",");
                sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
                sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

                resultIndex++;

            }//for
            logger.info(sb.toString());
            logger.info("######## End test #3 ########");
        }catch (Exception e){
            logger.warning("doSqlTest3",e);
        }
    }

    /**
     * Are searches case-insensitive?
     * @param con
     */
    private static void doSqlTest4(){
        try{
            logger.info("######## Start test #4 - case insensitive search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> resultsUpperCase =  mySqlQueries.doStudySearch("patientName","A*");
            List<Map<String,String>> resultsLowerCase =  mySqlQueries.doStudySearch("patientName","a*");

            if(resultsUpperCase.size()!=resultsLowerCase.size()){
                logger.info("FAILED: Case insensitive search test."
                        +" upper-case="+resultsUpperCase.size()
                        +" lower-case="+resultsLowerCase.size());
            }else{
                logger.info("PASSES (likely) upper-case and lower-case search had same result. #"
                        +resultsLowerCase.size());
            }
            logger.info("######## End test #4 - case insensitive search ########");
        }catch (Exception e){
            logger.warning("doSqlTest4",e);
        }
    }


    /**
     * Test patientId search.
     */
    private static void doSqlTest5(){
        try{
            logger.info("######## Start test #5 - patient id search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results =  mySqlQueries.doStudySearch("patientId","2228*");

            String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Study Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                //sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
                sb.append("studyId=").append(currResult.get(keys[0])).append(",");
                sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
                sb.append("modality=").append(currResult.get(keys[2])).append(",");
                sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
                sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

                resultIndex++;
            }//for
            logger.info(sb.toString());
            logger.info("######## End test #5 ########");
        }catch (Exception e){
            logger.warning("doSqlTest5",e);
        }
    }

    private static void doSqlTest6(){
        try{
            logger.info("######## Start test #6 - exam-type search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results =  mySqlQueries.doStudySearch("examType","DX");

            String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Study Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
                sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
                sb.append("modality=").append(currResult.get(keys[2])).append(",");
                sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
                sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

                resultIndex++;
            }//for
            logger.info(sb.toString());
            logger.info("######## End test #6 ########");
        }catch (Exception e){
            logger.warning("doSqlTest6",e);
        }
    }

    private static void doSqlTest7(){
        try{
            logger.info("######## Start test #7 - study-time search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results =  mySqlQueries.doStudySearch("studyDate","2002");

            String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Study Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
                sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
                sb.append("modality=").append(currResult.get(keys[2])).append(",");
                sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
                sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

                resultIndex++;
            }//for
            logger.info(sb.toString());
            logger.info("######## End test #7 ########");
        }catch (Exception e){
            logger.warning("doSqlTest7",e);
        }
    }

    /**
     * Test a get"series call".
     */
    private static void doSqlTest8(){
        try{
            logger.info("######## Start test #8 - study-time search ########");

            MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
            List<Map<String,String>> results = mySqlQueries.doSeriesSearch("1.2.826.0.1.3680043.8.420.30757817405477639080180001130587461759");

            //String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
            StringBuilder sb = new StringBuilder("Series Results \n");
            int resultIndex=1;
            for(Map<String,String> currResult : results){

                sb.append("#").append(resultIndex).append(" ");
                sb.append(printKeys(currResult.keySet()));
                resultIndex++;

            }//for
            logger.info(sb.toString());
            logger.info("######## End test #8 ########");
        }catch (Exception e){
            logger.warning("doSqlTest8",e);
        }
    }

    /**
     * If "arg[0]" contains a lower-case 'r' use the rufus version of the connection string.
     * @param args String[]
     * @return String
     */
    private static String getConnectionStringFromCmdLineArgs(String[] args){
        String retVal = mysqlConn;

        if(args==null){
            System.out.println("args is null.");
        }else if(args.length==0){
            System.out.println("args length is zero.");
        }else if(args[0].indexOf('r')>-1){
            System.out.println("args[]= "+args[0]);
            retVal = mysqlConnR;
        }
        System.out.println("Using connection string: "+retVal);
        return retVal;
    }

    private static String printKeys(Set<String> keySet){
        StringBuilder sb = new StringBuilder();
        for(String currKey : keySet){
            sb.append(currKey).append(", ");
        }
        return sb.toString();
    }

    /**
     * Make a long string a short string so it displays better.
     * @param longString
     * @return
     */
    private static String truncate(String longString, int maxLen){

        if(longString.length()<maxLen){
            return longString;
        }

        //Shorten a long string.
        StringBuilder sb = new StringBuilder();
        sb.append(longString.substring(0,((maxLen/2)-3)));
        sb.append("...");
        int endIndex = longString.length()-(maxLen/2)+3;
        sb.append(longString.substring(endIndex));

        return sb.toString();
    }

    private static String truncate(String longString){
        return truncate(longString,20);
    }

    private static void logConnectionStatus(){
        StringBuilder sb = new StringBuilder("Connection status - ");

        sb.append(" #avail: ").append(MySqlInstance.getInstance().getConnectionPoolAvailCount());
        sb.append(" #used: ").append(MySqlInstance.getInstance().getConnectionPoolUsedCount());

        logger.info(sb.toString());
    }

}//class
