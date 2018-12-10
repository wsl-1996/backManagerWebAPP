package com.skqtec.controller;

public class merchantsTest {
/*
    @org.junit.Before
    public void setUp() throws Exception {

    }



    @org.junit.Test
    public void main() {
        try {
            //SessionFactory sessionFactory=new SessionFactoryImpl();
            Class.forName("com.mysql.jdbc.Driver") ;
        }catch(ClassNotFoundException e){
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace() ;
        }
            String url = "jdbc:mysql://47.99.78.252:3306/ketuanDB_test?useUnicode=true&characterEncoding=utf-8";
            String username = "root";
            String password = "01space.org";
            Statement stmt=null;
            try{
                Connection con=DriverManager.getConnection(url , username , password );
                 stmt=con.createStatement() ;
            }catch(SQLException se){
                System.out.println("数据库连接失败！");
                se.printStackTrace() ;
            }
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\ProductClassifyCode.txt")));
                String data=null;
                int k=0;
                while((data=br.readLine())!=null){
                char a[]=data.toCharArray();
                int i;
                for(i=0;a[i]!='(';i++);
                String name=data.substring(0,i);
                String code=data.substring(i+1,i+7);
                System.out.println(name+"***"+code);
                int row=0;
                do{
                    row=stmt.executeUpdate("insert into PRODUCTCLASSIFYCODE values('"+k+"','"+code+"','"+name+"');");
                }while(row!=1);
                k++;
                }
            }catch (Exception e){
                e.printStackTrace();
        }
    }

   /* public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            javax.jms.TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }

    @Autowired
    private GroupRepository groupRepository;

    @org.junit.Test
    public void getAllMerchants() {
        ResponseData responseData = new ResponseData();
        try {
            /*Map<String,String> reqData=new HashMap<String, String>();
            reqData.put("appId","wx5733cafea467c980");
            reqData.put("nonceStr","fjVOtMdZ9f0ALW74");
            reqData.put("package","prepay_id=wx03174838573134b762d0256c3218565446");
            reqData.put("signType","MD5");
            reqData.put("timeStamp","1533289949");
            String paySign=WXPayUtil.generateSignature(reqData,"lijie1987110801spaceorg1234qwerl",WXPayConstants.SignType.MD5);
            System.out.println(paySign);*/

            /*int totalMoney=cashBackRepository.statisticCashback("01");
            System.out.println(totalMoney);*/
            /*GroupEntity groupEntity=groupRepository.get("04");
            Timestamp endTime=groupEntity.getEndTime();
            ChangeGroupState.addToRedis("04",endTime);
        } catch (Exception e) {
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.TEST_FAILED);
        } finally {

        }
    }
*/

}