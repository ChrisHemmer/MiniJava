  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        1
  5         LOAD         3[LB]
  6         CALL         putintnl
  7         LOADL        50
  8         STORE        3[LB]
  9         LOAD         3[LB]
 10         CALL         putintnl
 11         RETURN (0)   1
 12         LOADL        0
 13         JUMP         L12
 14  L11:   LOADL        0
 15         CALL         putintnl
 16         LOAD         3[LB]
 17         LOADL        1
 18         CALL         add     
 19         STORE        3[LB]
 20  L12:   LOAD         3[LB]
 21         LOADL        5
 22         CALL         lt      
 23         JUMPIF (1)   L11
 24         RETURN (0)   0
 25         LOADL        15
 26         STORE        0[OB]
 27         LOAD         0[OB]
 28         CALL         putintnl
 29         RETURN (0)   0
 30         LOAD         -1[LB]
 31         STORE        0[OB]
 32         RETURN (0)   1
