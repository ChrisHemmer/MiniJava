  0         PUSH         1
  1         LOADL        0
  2         STORE        0[SB]
  3         LOADL        0
  4         CALL         newarr  
  5         CALL         L11
  6         HALT   (0)   
  7  L10:   LOAD         0[OB]
  8         LOAD         -1[LB]
  9         CALL         add     
 10         STORE        0[OB]
 11         LOAD         0[SB]
 12         LOAD         -1[LB]
 13         CALL         add     
 14         STORE        0[SB]
 15         RETURN (0)   1
 16  L11:   LOADL        -1
 17         LOADL        1
 18         CALL         newobj  
 19         LOADL        15
 20         LOAD         3[LB]
 21         CALLI        L10
 22         LOADL        -1
 23         LOADL        1
 24         CALL         newobj  
 25         LOADL        12
 26         LOAD         4[LB]
 27         CALLI        L10
 28         LOAD         3[LB]
 29         LOADL        0
 30         CALL         fieldref
 31         CALL         putintnl
 32         LOAD         4[LB]
 33         LOADL        0
 34         CALL         fieldref
 35         CALL         putintnl
 36         LOAD         0[SB]
 37         CALL         putintnl
 38         RETURN (0)   1
