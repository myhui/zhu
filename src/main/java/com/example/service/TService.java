package com.example.service;

import com.example.dao.ITdao;
import com.example.model.TMain;
import com.example.model.TextType;
import com.example.model.Tmodel;
import com.example.model.Ttu;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25.
 */

@Service
public class TService {
    public static final Logger logger = LoggerFactory.getLogger(TService.class);

    private final int pageSize = 100;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Tmodel> getList(){
        String sql = "SELECT *   FROM zhu";
        return (List<Tmodel>) jdbcTemplate.query(sql, new RowMapper<Tmodel>(){

            @Override
            public Tmodel mapRow(ResultSet rs, int rowNum) throws SQLException {
                Tmodel stu = new Tmodel();
                stu.setT1(rs.getString("t1"));
                stu.setT2(rs.getString("t2"));
                stu.setT3(rs.getString("t3"));
                stu.setT4(rs.getString("t4"));
                stu.setT5(rs.getString("t5"));
                stu.setT6(rs.getString("t6"));
                stu.setT7(rs.getString("t7"));
                return stu;
            }

        });
    }

    public long getCount(long tId){
        String sql = "SELECT count(1)   FROM zhu WHERE t_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[] {tId },Long.class);
    }

    public long updateTuDnum(long tid, int n){
        String sql = "UPDATE t_tu SET d_num = ? WHERE id= ?;";

        jdbcTemplate.update(new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setInt(1, n);
                                    ps.setLong(2, tid);
                                    return ps;
                                }
                            }
        );
        return 0;
    }


    public long insertTMain(TMain main){
        //INSERT INTO zhulong_t.t_mian (id, t_id, title, hot, fbsj, liulan, download_num, sjf, weizhi, fenlie, neirong, tu_num, shejishi, biaoqian, miaoshu, tu, tu_id, tu_desc) VALUES (1, 1, '1', 1, '1', 1, 1, '1', '1', '1', '1', 1, '1', '1', '1', '1', 1, '1');
        String sql = "INSERT INTO zhulong_t.t_mian (t_id, title, hot, fbsj, liulan, download_num, sjf, weizhi, fenlie, neirong, tu_num, shejishi, biaoqian, miaoshu, tu, tu_id, tu_desc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return updateTMain(main,sql);
    }

    public long updateTMain(TMain main){
        String sql2 = "INSERT INTO zhulong_t.t_mian (t_id, title, hot, fbsj, liulan, download_num, sjf, weizhi, fenlie, neirong, tu_num, shejishi, biaoqian, miaoshu, tu, tu_id, tu_desc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       if(main == null){
           return 0;
       }
        StringBuilder sql = new StringBuilder("UPDATE FROM t_mian as m SET ");

        if(!StringUtils.isEmpty(main.getTitle())){
            sql.append("m.title = :title,");
        }

        //"m.title = ?, m.hot = ?, m.fbsj = ?, liulan = ? WHERE m.t_id = ?";

        sql.append(" 1=1 WHERE m.t_id = :tId");
        return updateTMain(main,sql.toString());
    }


    private long updateTMain(TMain main, String sql){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        long autoIncId = 0;

        jdbcTemplate.update(new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setLong(1, main.gettId());
                                    ps.setString(2, main.getTitle());
                                    ps.setInt(3, main.getHot());
                                    ps.setString(4, main.getFbsj());
                                    ps.setLong(5, main.getLiulan());
                                    ps.setLong(6, main.getDownloadnum());
                                    ps.setString(7, main.getSjf());
                                    ps.setString(8, main.getWeizhi());
                                    ps.setString(9, main.getFenlie());
                                    ps.setString(10, main.getNeirong());
                                    ps.setString(11, main.getTupiannum());
                                    ps.setString(12, main.getShejishi());
                                    ps.setString(13, main.getBiaoqian());
                                    ps.setString(14, main.getMiaoshu());
                                    ps.setString(15, main.getTu());
                                    ps.setLong(16, main.getTuId());
                                    ps.setString(17, main.getTudesc());

                                    return ps;
                                }
                            },keyHolder
        );

        autoIncId = keyHolder.getKey().intValue();

        return autoIncId;
    }

    public long insertAndUpdateTMain(TMain mian){
        if(mian==null || mian.gettId() < 1){
            logger.warn("main  tId is null {}",mian.getTitle());
        }
        long count =getCount(mian.gettId());

        long id = 0;
        if(count>0){
            id = updateTMain(mian);
        }else {
            id = insertTMain(mian);
        }
        return id;
    }


    public long insertAndUpdateTtu(Ttu tu){
        //// TODO: 16/5/25 插入更新
        return 0;
    }



    public long insertTtu(Ttu tu){
        //INSERT INTO zhulong_t.t_mian (id, t_id, title, hot, fbsj, liulan, download_num, sjf, weizhi, fenlie, neirong, tu_num, shejishi, biaoqian, miaoshu, tu, tu_id, tu_desc) VALUES (1, 1, '1', 1, '1', 1, 1, '1', '1', '1', '1', 1, '1', '1', '1', '1', 1, '1');
        String sql = "INSERT INTO zhulong_t.t_tu (id, mid, `name`, turl, width, `desc`,`t_num`,`title`) VALUES (?, ?, ?, ?, ?, ?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        long autoIncId = 0;

        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, tu.getId());
                ps.setLong(2, tu.getMid());
                ps.setString(3, tu.getName());
                ps.setString(4, tu.getTurl());
                ps.setInt(5, tu.getWidth());
                if(tu.getDesc()==null){
                    tu.setDesc("");
                }
                ps.setString(6, tu.getDesc());

                Map<String,Object> m =tools.vTuNum(tu.getDesc());

                int tunm = StringUtils.isEmpty(m.get("n"))?0:(int)m.get("n");
                ps.setInt(7,tunm);

                String tuTitle = StringUtils.isEmpty(m.get("t"))?"":m.get("t").toString();

                ps.setString(8, tuTitle);


                return ps;

            }
        },keyHolder

        );

        autoIncId = keyHolder.getKey().intValue();

        return autoIncId;

    }

    int dNum = 1;
    public void downloadImages(int page, int size){
        List<Ttu> list = getTuListByPage(page,size);
        for(Ttu t:list){
            String imgUrl = t.getTurl();
            String ts = genImagesTitle(t);

            if("".equals(ts)){
                logger.error("图片下载错误"+t.getId());
                continue;
            }
            boolean isT = writeImg(imgUrl,ts);
            if (isT){
                updateTuDnum(t.getId(), dNum);
            }

        }
    }

    public String genImagesTitle(Ttu t){
        String ttt = "";
        if(t!=null&&t.getId()!=0){
            ttt=t.getMid()+"\\"+t.getMid()+"_"+t.getId()+"_"+t.gettNum();
        }
        return ttt;
    }

    public List<Ttu> getTuListByPage(int page, int size){
        if(size == 0){
            size = pageSize;
        }
        int start = (page-1)*size;
        String sql = "SELECT *   FROM t_tu LIMIT "+start+","+pageSize;
        return (List<Ttu>) jdbcTemplate.query(sql, new RowMapper<Ttu>(){

            @Override
            public Ttu mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ttu stu = new Ttu();
                stu.setId(rs.getLong("id"));
                stu.setMid(rs.getLong("mid"));
                stu.setName(rs.getString("name"));
                stu.setTurl(rs.getString("turl"));
                stu.setWidth(rs.getInt("width"));
                stu.setDesc(rs.getString("desc"));
                stu.setTitle(rs.getString("title"));
                stu.settNum(rs.getInt("t_num"));
                stu.setdNum(rs.getInt("d_num"));

                return stu;
            }

        });
    }


    public List<Tmodel> getListByPage(int page, int size){

        int start = (page-1)*pageSize;
        String sql = "SELECT *   FROM zhu LIMIT "+start+","+pageSize;
        return (List<Tmodel>) jdbcTemplate.query(sql, new RowMapper<Tmodel>(){

            @Override
            public Tmodel mapRow(ResultSet rs, int rowNum) throws SQLException {
                Tmodel stu = new Tmodel();
                stu.setT1(rs.getString("t1"));
                stu.setT2(rs.getString("t2"));
                stu.setT3(rs.getString("t3"));
                stu.setT4(rs.getString("t4"));
                Blob noteBlob = rs.getBlob("t5");

                String note = null;
                try {

                    if (noteBlob != null) {
                        InputStream is = noteBlob.getBinaryStream();
                        ByteArrayInputStream bais = (ByteArrayInputStream) is;
                        byte[] byte_data = new byte[bais.available()]; //bais.available()返回此输入流的字节数
                        bais.read(byte_data, 0, byte_data.length);//将输入流中的内容读到指定的数组
                        note = new String(byte_data, "utf-8"); //再转为String，并使用指定的编码方式
                        is.close();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stu.setT5(note);
                stu.setT6(rs.getString("t6"));
                stu.setT7(rs.getString("t7"));
                return stu;
            }

        });
    }

    private static int nums = 0;

    public TextType t2(Node node){
        //
        TextType t=null;

        //获取子节点
        NodeList nodes = node.getChildren();

        if(nodes!=null) {
            for (int i = 0; i < nodes.size(); i++) {
                Node zNode = (Node) nodes.elementAt(i);

                String zText = zNode.getText().trim();

                if(zText.startsWith("img")) {

                    TagNode tagNode = new TagNode();
                    String toHtml = zNode.toHtml();
                    tagNode.setText(toHtml);

                    if ("lazy".equals(tagNode.getAttribute("class"))) {
                        nums++;

                        t = TextType.FTU;
                        Map<String,Object> m = new HashMap<String,Object>();

                        m.put("nums",nums);
                        m.put("turl",tagNode.getAttribute("data-original"));
                        m.put("alt", tagNode.getAttribute("alt"));
                        m.put("title",tagNode.getAttribute("title"));
                        m.put("width", tagNode.getAttribute("width"));

                        t.setM(m);

                        return t;

                    } else if (tagNode!=null) {
                        nums++;

                        t = TextType.MTU;
                        Map<String,Object> m = new HashMap<String,Object>();

                        m.put("nums",nums);
                        m.put("turl",tagNode.getAttribute("src"));
                        m.put("alt", tagNode.getAttribute("alt"));
                        m.put("title",tagNode.getAttribute("title"));

                        t.setM(m);

                        return t;
                    }
                }

            }
        }

        return t;
    }

    public TextType t1(String cText){

        TextType t=null;

        if(cText.startsWith("设计方：")){
            t = TextType.SJF;
            t.setContext(cText.replace("设计方：",""));

        }else if(cText.startsWith("位置：")){
            t = TextType.WEIZHI;
            t.setContext(cText.replace("位置：",""));

        }else if(cText.startsWith("分类：")){
            t = TextType.FENLIE;
            t.setContext(cText.replace("分类：",""));

        }else if(cText.startsWith("内容：")){
            t = TextType.NEIRONG;
            t.setContext(cText.replace("内容：",""));

        }else if(cText.startsWith("摄影师：")){
            t = TextType.SHEYINSHI;
            t.setContext(cText.replace("摄影师：",""));

        }else if(cText.startsWith("标签：")){
            t = TextType.BIAOQIAN;
            t.setContext(cText.replace("标签：",""));

        }else if(cText.length()> 50){
            t = TextType.MIAOSHU;
            t.setContext(cText);

        }else if(cText.startsWith("图片：")){
            t = TextType.TUPIAN;
            t.setContext(cText.replace("图片：","").replace("张",""));

        }else if(cText.startsWith("安装图酷APP可以免币下载无水印高清大图")){

        }else if(cText.startsWith("成本：")){
            t = TextType.CB;
            t.setContext(cText.replace("成本：",""));
        }else if(cText.startsWith("设计团队：")){
            t = TextType.SJTD;
            t.setContext(cText.replace("设计团队：",""));
        }else if(cText.startsWith("合作建筑师：")){
            t = TextType.HZJZS;
            t.setContext(cText.replace("合作建筑师：",""));
        } else if(cText.startsWith("项目合作伙伴：")){
            t = TextType.XMHZHB;
            t.setContext(cText.replace("项目合作伙伴：",""));
        }else if(cText.startsWith("委托方：")){
            t = TextType.WJF;
            t.setContext(cText.replace("委托方：",""));
        }else if(cText.startsWith("图片来源：")){
            t = TextType.TPLY;
            t.setContext(cText.replace("图片来源：",""));
        }else if(cText.startsWith("合作方：")){
            t = TextType.HZF;
            t.setContext(cText.replace("合作方：",""));
        }else if(cText.startsWith("承包方：")){
            t = TextType.CBF;
            t.setContext(cText.replace("承包方：",""));
        }else if(cText.startsWith("开发商：")){
            t = TextType.KFS;
            t.setContext(cText.replace("开发商：",""));
        }else if(cText.startsWith("项目合作伙伴：")){
            t = TextType.HZHB;
            t.setContext(cText.replace("项目合作伙伴：",""));
        }else if(cText.startsWith("建筑师：")) {
            t = TextType.JZS;
            t.setContext(cText.replace("建筑师：", ""));
        }else if(cText.startsWith("设计师：")){
            t = TextType.SJS;
            t.setContext(cText.replace("设计师：",""));
        }else if(cText.startsWith("建筑公司：")){
            t = TextType.JZGS;
            t.setContext(cText.replace("建筑公司：",""));
        }else {
            logger.info("没有设置 ， 内容：{}",cText);
        }

        return t;
    }

    public String hp(String content,String title, long id) {
        try{

            if(StringUtils.isEmpty(content)){
                System.out.println("=content is null="+title+"=="+id);
                logger.info("==context is null,titile  id  {}|{}",title, id);
            }
            Parser parser = new Parser(content);
            NodeFilter filter = new TagNameFilter("p");
            NodeList nodes =  parser.extractAllNodesThatMatch(filter);

            if(nodes!=null) {
                TMain tm = new TMain();
                tm.settId(id);
                tm.setTitle(title);
//                tm.setHot();

                Ttu tu = new Ttu();
                tu.setMid(id);
                tu.setName(title);


                for (int i = 0; i < nodes.size(); i++) {
                    Node textnode = (Node) nodes.elementAt(i);

                    Node p = textnode.getParent();
                    if(p==null){

                        String cText = textnode.toPlainTextString().trim();

                        if(!"".equals(cText)){
                            TextType ty = t1(cText);

                            if(ty == null)
                                continue;



                            if (ty.getType() == TextType.SJF.getType()){
                                tm.setSjf(ty.getContext());
                            }else if(ty.getType() == TextType.WEIZHI.getType()){
                                tm.setWeizhi(ty.getContext());
                            }else if(ty.getType() == TextType.FENLIE.getType()){
                                tm.setFenlie(ty.getContext());
                            }else if(ty.getType() == TextType.NEIRONG.getType()){
                                tm.setNeirong(ty.getContext());
                            }else if(ty.getType() == TextType.TUPIAN.getType()){
                                tm.setTupiannum(ty.getContext());
                            }else if(ty.getType() == TextType.SHEYINSHI.getType()){
                                tm.setShejishi(ty.getContext());
                            }else if(ty.getType() == TextType.BIAOQIAN.getType()){
                                tm.setBiaoqian(ty.getContext());
                            }else if(ty.getType() == TextType.MIAOSHU.getType()){
                                tm.setMiaoshu(ty.getContext());
                            }else if(ty.getType() == TextType.CB.getType()){
                                tm.setCb(ty.getContext());
                            }

                            else if(ty.getType() == TextType.SJTD.getType()){
                                tm.setSjtd(ty.getContext());
                            }else if(ty.getType() == TextType.HZJZS.getType()){
                                tm.setHzjzs(ty.getContext());
                            }else if(ty.getType() == TextType.XMHZHB.getType()){
                                tm.setXmhzhb(ty.getContext());
                            }else if(ty.getType() == TextType.WJF.getType()){
                                tm.setWjf(ty.getContext());
                            }else if(ty.getType() == TextType.TPLY.getType()){
                                tm.setTply(ty.getContext());
                            }else if(ty.getType() == TextType.CBF.getType()){
                                tm.setCbf(ty.getContext());
                            }else if(ty.getType() == TextType.HZF.getType()){
                                tm.setCb(ty.getContext());
                            }else if(ty.getType() == TextType.KFS.getType()){
                                tm.setKfs(ty.getContext());
                            }else if(ty.getType() == TextType.HZHB.getType()){
                                tm.setHzhb(ty.getContext());
                            }else if(ty.getType() == TextType.JZS.getType()){
                                tm.setJzs(ty.getContext());
                            }else if(ty.getType() == TextType.JZGS.getType()){
                                tm.setJzgs(ty.getContext());
                            }else if(ty.getType() == TextType.SJS.getType()){
                                tm.setSjs(ty.getContext());
                            }


                        }else {
                            TextType t = t2(textnode);
                            if(t==null)
                                continue;

                            if(t.getType() == TextType.MTU.getType() || t.getType() == TextType.FTU.getType()){

                                Map<String,Object>  m = t.getM();

                                // 表崔图片
                                tu.setTurl(StringUtils.isEmpty(m.get("turl"))?"":m.get("turl").toString());
                                tu.setDesc(StringUtils.isEmpty(m.get("title"))?"":m.get("title").toString());
                                tu.setWidth(500);

                                insertTtu(tu);

                            }

                            if(t.getType() == TextType.MTU.getType()){

                                Map<String,Object>  m = t.getM();

                                tm.setTu(StringUtils.isEmpty(m.get("turl"))?"":m.get("turl").toString());
                                tm.setTuId(0);
                                tm.setTudesc(StringUtils.isEmpty(m.get("alt"))?"":m.get("alt").toString());
                            }
                        }

                    }

                }

                //biaocun
                insertTMain(tm);

            }

        }catch (ParserException pe){
            System.out.println("Exception : "+ pe);
        }
        catch( Exception e ) {

            // 133730 , 133918  nullPointer
            logger.error("错误信息：{}|{}", id, e);
            System.out.println( "Exception:"+e );
        }

    return "";
    }

    String baseUrl = "http://photo.zhulong.com/proj/detail";
    public long getTid(String t2) {

        String tt = t2.replace(".html","").replace(baseUrl,"");
        try {
            return Long.parseLong(tt);
        }catch (Exception e){

        }
        return 0;

    }

    public static final String imgPath="E:\\Test";

    public static boolean writeImg(String imgUrl, String imgTitle){



        BufferedImage image = null;
        try {
            String ex_name = tools.getExtensionNameForPath(imgUrl);
            if(ex_name==null || "".equals(ex_name)){
                ex_name = "jpg";
            }
            String path = imgPath+"\\"+imgTitle+"."+ex_name;

            File ifile = new File(path);
            if(ifile.exists()){
                logger.warn("图片已存在："+path);
                return false;
            }
            tools.createDir(imgPath+"\\"+imgTitle);
            URL url = new URL(imgUrl);
            image = ImageIO.read(url);


            File dirFile = new File(imgPath);
            if(!dirFile.exists()){
                dirFile.mkdir();
            }

            ImageIO.write(image, ex_name, ifile);
//            ImageIO.write(image, "gif", new File("E:\\Test\\classicplus.gif"));
//            ImageIO.write(image, "png", new File("E:\\Test\\classicplus.png"));

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getBx(String imgUrl){
        String bx="";
        if(imgUrl.length()>4){

        }

        return bx;
    }

    public static void main(String[] args) {
        String tt = tools.createDir("http://static.zhulong.com/photo/small/201603/04/143740itqhnxgcanckxr1v_0_0_560_w_0.jpg");

        System.out.print(tt);
    }

}
