package com.example.service;

import com.example.dao.ITdao;
import com.example.model.Tmodel;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */

@Service
public class TService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private final int pageSize = 1;
    public List<Tmodel> getListByPage(int page){
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

    public int panduan(Node node){
        int type = 0;

        String cText = node.toPlainTextString().trim();

        if(cText.startsWith("设计方：")){
            System.out.println("is 设计方。");
        }else if(cText.startsWith("位置：")){
            System.out.println("is 位置。");
        }else if(cText.startsWith("分类：")){
            System.out.println("is 分类。");
        }else if(cText.startsWith("内容：")){
            System.out.println("is 内容。");
        }else if(cText.startsWith("摄影师：")){
            System.out.println("is 摄影师。");
        }else if(cText.startsWith("标签：")){
            System.out.println("is 标签。");
        }else if(cText.length()> 100){
            System.out.println(" is Content");
        }

        Node zNode = node.getFirstChild();
        String zText = zNode.getText();

        HasAttributeFilter filter = new HasAttributeFilter("class");
        if(zText.startsWith("img")&& ){

        }




        return type;
    }

    public String hp(String content) {

        try{
            Parser parser = new Parser(content);
            NodeFilter filter = new TagNameFilter("p");
            NodeList nodes =  parser.extractAllNodesThatMatch(filter);

            if(nodes!=null) {
                for (int i = 0; i < nodes.size(); i++) {
                    Node textnode = (Node) nodes.elementAt(i);

                    Node p = textnode.getParent();
                    if(p==null){
                        System.out.println("getText:"+textnode.toPlainTextString().trim());
                        panduan(textnode);
                        System.out.println("=================================================");
                    }

                }
            }

//            for (Node node:nodes ) {
//                Node node = i.nextNode();
//                System.out.println("getText:"+node.getText());
//                System.out.println("getPlainText:"+node.toPlainTextString());
//                System.out.println("toHtml:"+node.toHtml());
//                System.out.println("toHtml(true):"+node.toHtml(true));
//                System.out.println("toHtml(false):"+node.toHtml(false));
//                System.out.println("toString:"+node.toString());
//                System.out.println("=================================================");
//            }
        }catch (ParserException pe){
            System.out.println("Exception : "+ pe);
        }
        catch( Exception e ) {
            System.out.println( "Exception:"+e );
        }

    return "";
    }
}