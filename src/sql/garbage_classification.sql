    --创建数据库（sys）
    --创建了用户相当于就创建了一个数据库
    --经常模式名都为数据库用户名，不理解看下面的链接
    --https://www.cnblogs.com/qiuhong10/p/7883156.html
    create user garbage_classification identified by M4an$WM9vFylHSJT;
    --授予权限（sys）,为了安全尽量不要授予unlimited tablespace权限
    -- （这个权限可以在其他表空间里随意建表），
    grant create session, create table, create view, create procedure,
        create trigger, create synonym, create tablespace, create sequence
        to garbage_classification;
    /*修改用户的表空间（sys）,好像只有sys能修改数据库默认表空间*/
    create tablespace garbage_classification_tbs
        datafile 'garbage_classification_tbs1.DBF'
        size 20 m--size必须指定
        extent management local uniform size 512 K;
    alter user garbage_classification default tablespace garbage_classification_tbs;
    --好像不能修改某个用户的默认临时表空间，默认表空间应该都是共用的
    --授予用户使用表空间的权限，使得用户可以想表中插入数据（代替 unlimited tablespace）（sys）
    alter user garbage_classification quota unlimited on garbage_classification_tbs;

    /*用下面的语句看权限只能查看到当前登录用户的权限
    用户被授予角色的权限在这里查不出来，但是可以使用*/
    select *
    from user_sys_privs;
    drop table user1;
    drop table garbageInfo;

    --创建用户表
    create table user1
    (
        username         varchar2(50) primary key,
        password         varchar2(64) not null,
        isAdmin          number(1) default 0,
        registrationTime date,
        lastLoginTime   date,
        userId           number unique,
        userRemarks      varchar2(200)
    );
    -- 用触发器实现id递增
    create sequence seq_id_user;
    create or replace trigger tri_insert_user
        before insert
        on user1
        for each row
    begin
        select seq_id_user.nextval
        into :new.userId
        from dual;--id飘红也能用，这个ide有点bug
    end;
    /
    /*必须用set role dev1 identified by rggg56y6545r4t5;激活角色，用户才有角色的权限
      每次登录都要激活 */
    --   直接把权限赋给用户吧，否则太麻烦

    --查看用户的表空间
    select username, default_tablespace
    from user_users;
    --向user1中插入一些样例
    insert into user1 (username, password)
    values ('admin', '123456');
    insert into user1 (username, password)
    values ('user1', '123456');
    insert into user1 (username, password)
    values ('user2', '123456');
    /*自增设置完成*/
    --删除临时表空间也用的drop tablespace 。
    --drop tablespace garbage_classification_temtbs;

    --创建垃圾信息表
    create table garbageInfo
    (
        garbage        varchar2(50) primary key,
        type           varchar2(20) not null,
        describe       varchar2(200),
        username       varchar2(50) references user1 (username),
        createTime     date,
        lastQueryTime  date,
        garbageId      number unique,
        garbageRemarks varchar2(200)
    ) tablespace garbage_classification_tbs;
    create sequence seq_id_garbage;
    create or replace trigger tri_insert_garbage
        before insert
        on garbageInfo
        for each row
    begin
        select seq_id_garbage.nextval
        into :new.garbageId
        from dual;--id飘红也能用，这个ide有点bug
    end;
    /
    insert into garbageInfo (garbage, type, username) values ('湿纸巾', '干垃圾', 'admin');
    insert into garbageInfo (garbage, type, username)
    values ('玻璃渣', '可回收垃圾', 'user1');
    insert into garbageInfo (garbage, type, username)
    values ('剩菜', '湿垃圾', 'admin');

    --修改isAdmin的默认值
    --alter table user1 modify (isAdmin number(1) default 0);
    -- user1表的存储过程，增删查改
    -------------------------------------------------------------------
    create or replace package pak_user is
        --存储过程的规范里没有参数的类型和参数的长度,必须指明参数输入输出类型
        --用于用户登录时验证什么类型
        PROCEDURE query_user_isAdmin(username1 in VARCHAR2,
                                     password1 in VARCHAR2,
                                     isAdmin1 out number);

        --用于管理员查看用户信息（这个功能最后写）
        Procedure query_user_info(username1 in out VARCHAR2,
                                  isAdmin1 out number,
                                  registrationTime1 out date,
                                  lastLoginTime1 out date,
                                  userRemarks1 out VARCHAR2);

        --用于用户注册时添加用户信息
        Procedure insert_user(username1 in VARCHAR2,
                              password1 in VARCHAR2);
        --用于管理员删除用户信息
        Procedure delete_user(username1 in VARCHAR2);

        --用于管理员修改用户信息
        Procedure update_user(username1 in VARCHAR2,
                              isAdmin1 in number,
                              userRemarks1 in VARCHAR2);
        Procedure query_user_isAdmin_forAdmin(username1 in VARCHAR2,
                                              isAdmin1 out number);
    end pak_user;
    /

    -------------------------------------------------------------创建包体
    --存储过程参数名不能和列名相同，参数要和包中的声明一致,参数类型不能带长度
    create or replace package body pak_user is
        --用于管理员修改用户信息
        Procedure update_user(username1 in VARCHAR2,
                              isAdmin1 in number,
                              userRemarks1 in VARCHAR2) is
        begin
            update user1
            set isAdmin=isAdmin1,
                userRemarks=userRemarks1
            where username = username1;
            commit;
        end update_user;

        --delete_user用于管理员删除用户信息
        Procedure delete_user(username1 in VARCHAR2) is
        begin
            delete from user1 where username = username1;
            commit;
        end delete_user;

        --query_user_info管理员查询用户信息,只查这五个信息，其它的没意义

        Procedure query_user_info(username1 in out VARCHAR2,--这有一个可输入可输出的参数
                                  isAdmin1 out number,
                                  registrationTime1 out date,
                                  lastLoginTime1 out date,
                                  userRemarks1 out VARCHAR2) is
        begin
            select username, isAdmin, registrationTime, lastLoginTime, userRemarks
            into username1, isAdmin1, registrationTime1,lastLoginTime1,userRemarks1
            from user1
            where username = username1;
        end query_user_info;

        --insert_user用户注册时插入用户信息
        Procedure insert_user(username1 in VARCHAR2,
                              password1 in VARCHAR2) is
        begin
            insert into user1 (username, password, registrationTime)
            values (username1, password1, sysdate);
            commit;
        end insert_user;
        --query_user_isAdmin，用户在尝试登录
        Procedure query_user_isAdmin(username1 in VARCHAR2,
                                     password1 in VARCHAR2,
                                     isAdmin1 out number) is
        BEGIN
            --isAdmin1等于-2说明登录失败
            isAdmin1 := -2;
            select ISADMIN
            into isAdmin1
            from user1
            where username = username1
              and password = password1;
            --如果查到了isAdmin1的值，说明用户登录成功，此时应更新用户上一次登录的时间
            --dbms_output.put_line('isAdmin:' || isAdmin1);
            if isAdmin1 != -2 then
                update user1
                set lastLoginTime=sysdate
                where username = username1;
            end if;
            commit;
        END query_user_isAdmin;
        --query_user_isAdmin_when，用户在尝试登录
        Procedure query_user_isAdmin_forAdmin(username1 in VARCHAR2,
                                              isAdmin1 out number) is
        BEGIN
            select ISADMIN
            into isAdmin1
            from user1
            where username = username1;
        END query_user_isAdmin_forAdmin;

    end pak_user;
    /

    select lastLoginTime
    from user1;
    /*本来想通过用户名和密码返回用户的所有信息，这样就可以写一个数据库的逻辑让登录和查询用户信息的模块都能用，
      但是写了四个小时，发现record真的很难在java里面接收，最终不知道选什么类型的参数在jdbc中registerOutParameter，
      只好改成让存储过程只有一个number类型的输出参数...query_user变成了query_user_isAdmin
      编译出错用 select text from sys.user_errors where name='PAK_USER'; 看细节*/
    /*declare
        info pak_user.user_type;
    begin
        pak_user.query_user('admin', '123456', info);
        dbms_output.put_line('isAdmin:' || info.ISADMIN);
    end;
    /*/
    /*

    create PROCEDURE test_java(username1 out VARCHAR2) is
    begin
        dbms_output.put_line('test_java---' || username1);
    end test_java;
    declare
        www varchar2(10);
    begin
        pak_user.test_java(www);
    end;*/
    /*用户的增删查改写好了*/

    --------------------------------------------------------------------
    -- 接下里写垃圾表的存储过程
    create or replace package pak_garbage is
        --前端查询垃圾信时调用
        procedure query_garbage(garbage1 in out varchar2,
                                type1 out varchar2,
                                describe1 out varchar2);
        --有用户提交垃圾信息时调用
        procedure insert_garbage(garbage1 in varchar2,
                                 type1 in varchar2,
                                 describe1 in varchar2,
                                 username1 in varchar2,
                                 garbageRemarks1 in varchar2);
        --删除
        procedure delete_garbage(garbage1 in varchar2);
        --更新
        procedure update_garbage(garbage1 in varchar2,
                                 type1 in varchar2,
                                 describe1 in varchar2);
        --检查垃圾信息是否由所给用户创建
        procedure query_garbage_creater(garbage1 in out varchar2,
                                        username1 in varchar2);
        --查询所有垃圾信息
        procedure query_garbage_all(garbageAll out sys_refcursor);
    end pak_garbage;
    /
    ----------------------------------------------------
    ---------创建pak_garbage包体
    create or replace package body pak_garbage is
        --查询数据库中的所有垃圾信息
        procedure query_garbage_all(garbageAll out sys_refcursor)
            is
        begin
            open garbageAll for 'select garbage, type, describe from garbageInfo';
        end query_garbage_all;
        --检查垃圾信息是否由所给用户创建
        procedure query_garbage_creater(garbage1 in out varchar2,
                                        username1 in varchar2) is
        begin
            select garbage
            into garbage1
            from garbageInfo
            where garbage = garbage1
              and username = username1;
        end query_garbage_creater;

        --前端查询垃圾信时调用
        procedure query_garbage(garbage1 in out varchar2,
                                type1 out varchar2,
                                describe1 out varchar2) is
        begin
            --下面赋给这个flag的值的类型必须和变量的类型一致，都是varchar
            type1 := '2';
            select garbage, type, describe
            into garbage1, type1, describe1
            from garbageInfo
            where garbage = garbage1;
            if type1 != '2' then
                update garbageInfo set lastQueryTime=sysdate where garbage = garbage1;
            end if;
        end query_garbage;
    --有用户提交垃圾信息时调用
        procedure insert_garbage(garbage1 in varchar2,
                                 type1 in varchar2,
                                 describe1 in varchar2,
                                 username1 in varchar2,
                                 garbageRemarks1 in varchar2) is
        begin
            insert into garbageInfo (garbage, type, describe, username, garbageRemarks, createTime)
            values (garbage1, type1, describe1, username1, garbageRemarks1, sysdate);
            commit;
        end insert_garbage;
        --删除垃圾信息
        --后端传过来垃圾的名称就能删除，关于复杂的权限验证应放在后端。
        procedure delete_garbage(garbage1 in varchar2)
            is
        begin
            delete from garbageInfo where garbage = garbage1;
            commit;
        end delete_garbage;
        --更改垃圾信息,有些输入参数用户不一定填写，让前端传过来未更改输入参数
        --用户查询到的和能更改的数据一致
        procedure update_garbage(garbage1 in varchar2,
                                 type1 in varchar2,
                                 describe1 in varchar2)
            is
        begin
            update garbageInfo
            set garbage=garbage1,
                type=type1,
                describe=describe1
            where garbage = garbage1;
            commit;
        end update_garbage;
    end pak_garbage;
    /
    --垃圾信息表的存储过程写完了，测试通过。明天写servlet