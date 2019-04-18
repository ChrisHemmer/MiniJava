  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L16
  3         HALT   (0)   
  4  L10:   LOAD         -1[LB]
  5         LOADL        1
  6         CALL         eq      
  7         JUMPIF (1)   L11
  8         LOADL        0
  9         LOAD         -1[LB]
 10         LOADL        2
 11         CALL         eq      
 12         CALL         or      
 13         JUMP         L13
 14  L11:   LOADL        1
 15         JUMP         L13
 16  L12:   LOAD         -1[LB]
 17         LOADL        2
 18         CALL         eq      
 19         CALL         or      
 20  L13:   JUMPIF (1)   L14
 21         JUMP         L15
 22  L14:   LOADL        1
 23         RETURN (1)   1
 24         POP          0
 25  L15:   LOAD         -1[LB]
 26         LOADL        1
 27         CALL         sub     
 28         CALL         L10
 29         LOAD         -1[LB]
 30         LOADL        2
 31         CALL         sub     
 32         CALL         L10
 33         CALL         add     
 34         RETURN (1)   1
 35  L16:   LOADL        1
 36         JUMP         L18
 37  L17:   LOAD         3[LB]
 38         CALL         L10
 39         CALL         putintnl
 40         LOAD         3[LB]
 41         LOADL        1
 42         CALL         add     
 43         STORE        3[LB]
 44         POP          0
 45  L18:   LOAD         3[LB]
 46         LOADL        20
 47         CALL         lt      
 48         JUMPIF (1)   L17
 49         RETURN (0)   1
