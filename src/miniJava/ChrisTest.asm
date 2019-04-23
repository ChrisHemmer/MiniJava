  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        5
  5         LOADL        10
  6         LOADL        0
  7         LOAD         3[LB]
  8         LOAD         4[LB]
  9         CALL         add     
 10         LOAD         3[LB]
 11         CALL         mult    
 12         LOAD         3[LB]
 13         LOAD         4[LB]
 14         CALL         add     
 15         CALL         add     
 16         STORE        5[LB]
 17         LOAD         5[LB]
 18         CALL         putintnl
 19         RETURN (0)   1
