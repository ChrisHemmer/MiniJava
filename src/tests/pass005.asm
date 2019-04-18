  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        -1
  5         LOADL        2
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         LOADL        0
  9         LOADL        0
 10         CALL         fieldupd
 11         LOADL        -1
 12         LOADL        2
 13         CALL         newobj  
 14         LOAD         4[LB]
 15         LOADL        0
 16         LOADL        1
 17         CALL         fieldupd
 18         LOAD         4[LB]
 19         LOAD         3[LB]
 20         CALL         L11
 21         RETURN (0)   1
 22  L11:   LOADL        0
 23         CALL         putintnl
 24         LOAD         -1[LB]
 25         LOADL        0
 26         CALL         fieldref
 27         CALL         putintnl
 28         LOADL        1
 29         CALL         putintnl
 30         LOAD         -2[LB]
 31         LOADL        0
 32         CALL         fieldref
 33         CALL         putintnl
 34         LOAD         -1[LB]
 35         LOADL        0
 36         LOADL        3
 37         CALL         fieldupd
 38         LOAD         -2[LB]
 39         LOADL        0
 40         LOADL        2
 41         CALL         fieldupd
 42         LOAD         -1[LB]
 43         LOAD         -2[LB]
 44         CALL         L12
 45         RETURN (0)   2
 46  L12:   LOADL        2
 47         CALL         putintnl
 48         LOAD         -1[LB]
 49         LOADL        0
 50         CALL         fieldref
 51         CALL         putintnl
 52         LOADL        3
 53         CALL         putintnl
 54         LOAD         -2[LB]
 55         LOADL        0
 56         CALL         fieldref
 57         CALL         putintnl
 58         LOAD         -1[LB]
 59         LOADL        0
 60         LOADL        5
 61         CALL         fieldupd
 62         LOAD         -2[LB]
 63         LOADL        0
 64         LOADL        4
 65         CALL         fieldupd
 66         LOAD         -1[LB]
 67         LOAD         -2[LB]
 68         CALL         L13
 69         RETURN (0)   2
 70  L13:   LOADL        4
 71         CALL         putintnl
 72         LOAD         -1[LB]
 73         LOADL        0
 74         CALL         fieldref
 75         CALL         putintnl
 76         LOADL        5
 77         CALL         putintnl
 78         LOAD         -2[LB]
 79         LOADL        0
 80         CALL         fieldref
 81         CALL         putintnl
 82         RETURN (0)   2
