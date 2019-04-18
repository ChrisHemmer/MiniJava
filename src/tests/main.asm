  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        1
  5         CALL         putintnl
  6         LOADL        2
  7         CALL         putintnl
  8         LOADL        3
  9         CALL         putintnl
 10         LOADL        4
 11         CALL         putintnl
 12         LOADL        5
 13         CALL         putintnl
 14         LOADL        1
 15         JUMPIF (1)   L11
 16         JUMP         L12
 17  L11:   LOADL        6
 18         CALL         putintnl
 19         POP          0
 20         JUMP         L13
 21  L12:   LOADL        1
 22         CALL         neg     
 23         CALL         putintnl
 24         POP          0
 25         JUMP         L13
 26  L13:   RETURN (0)   1
