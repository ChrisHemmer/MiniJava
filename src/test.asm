  0  L10:   LOADL        0
  1         CALL         newarr  
  2         CALL         L11
  3         HALT   (0)   
  4  L11:   LOADL        -1
  5         LOADL        2
  6         CALL         newobj  
  7         LOADL        11111
  8         CALL         putintnl
  9         LOADL        52
 10         LOAD         13[LB]
 11         CALLI        L10
 12         RETURN (0)   1
 13         LOAD         1[LB]
 14         LOADL        1
 15         LOAD         -1[LB]
 16         CALL         fieldupd
 17         RETURN (0)   1
