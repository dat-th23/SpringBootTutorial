package java.com.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String name;
    private String username;
    private String password;

    @Lob
    private byte[] avatar;

    /*
     * FetchType.EAGER: When you load User table from database, it will automatically load Role table as well
     * FetchType.LAZY : When you load User table from database, it just loads User from DB, you have to call getAllRoles()
     * from User to get their roles
     * */

    /*
     * FetchType.EAGER: Khi bạn tải bảng User từ database, nó cũng sẽ auto tải bảng role
     * FetchType.LAZY: Khi bạn tải bảng User từ database, nó chỉ tải bảng User từ DB, bạn phải gọi tới function
     * getAllRoles() từ class User để lấy role của đối tượng
     * */

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles =  new ArrayList<>();


}
