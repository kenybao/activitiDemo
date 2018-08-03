package com.bbs;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 73598.
 * @Date 2018/8/2 0002.
 * @Time 16:53.
 */
public class NoContextTest {

    @Test
    public  void  map2Json(){
        Map map = new HashMap();
        map.put("1","2");
        System.out.println(JSONObject.toJSONString(map));
    }
    @Test
    public  void  json2Map(){
        String json = "{\"1\":\"2\"}";
        Map<String, String> stringStringMap = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {
        });
        Assert.isTrue(stringStringMap.get("1").equals("2"),"失败咯");
    }
    @Test
    public  void  List2Json(){
        Map map = new HashMap();
        map.put("1","2");
        Student student = new Student();
        student.setIdNo("1");
        student.setSex("boy");
        List<Student> list = new ArrayList<Student>();
        list.add(student);
        System.out.println(JSONObject.toJSONString(list));
//        Assert.isTrue(stringStringMap.get("1").equals("2"),"失败咯");
    }
    @Test
    public  void  Json2List(){
        String json = "[{\"idNo\":\"1\",\"sex\":\"boy\"}]";
        List<Student> students = JSONObject.parseObject(json, new TypeReference<List<Student>>() {
        });
        //切记 student 不能为 内部类 否则无法实例化 或
//        List<Student> students = JSONObject.parseArray(json, Student.class);
        System.out.println(students.get(0).toString());
//        Assert.isTrue(stringStringMap.get("1").equals("2"),"失败咯");
    }

    /**
     * 复杂对象转 json json转复杂对象
     */
    @Test
    public  void  complicateObject2JsonAndRevert(){
        Result<Teacher> result = new Result<Teacher>();
        Teacher teacher = new Teacher();
        teacher.setTeachClassName("老师1");
        result.setStatus("启用1");
        result.setT(teacher);

        String s = JSONObject.toJSONString(result);
        System.out.println(s);
        Result<Teacher> result1 = JSONObject.parseObject(s, new TypeReference<Result<Teacher>>() {
        });
        Assert.isTrue(result1.getStatus().equals(result.getStatus())&&result.getT().getTeachClassName().equals(result1.getT().getTeachClassName()),"错误");
    }

    /**
     * 复杂类型list 转 json  josn 转复杂类型list
     */
    @Test
    public  void  complicateObjectList2JsonAndRevert(){
        Result<Teacher> result = new Result<Teacher>();
        Teacher teacher = new Teacher();
        teacher.setTeachClassName("老师2");
        result.setStatus("启用2");
        result.setT(teacher);
        List list = new ArrayList(1);
        list.add(result);
        String s = JSONObject.toJSONString(list);
        System.out.println(s);
        List<Result<Teacher>> resultlists = JSONObject.parseObject(s, new TypeReference<List<Result<Teacher>>>() {
        });
        Assert.isTrue(resultlists.get(0).getStatus().equals(result.getStatus())&&result.getT().getTeachClassName().equals(resultlists.get(0).getT().getTeachClassName()),"错误");
    }



}
class  Result<T extends Person> implements Serializable{
    private String status;
    private T t;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Result() {
    }
}
class Teacher extends Person implements Serializable{
    private String teachClassName;

    public Teacher() {
    }

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }
}
class Student extends Person implements Serializable{
    private String idNo;

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "idNo='" + idNo + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
abstract class Person implements Serializable{
    protected String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
