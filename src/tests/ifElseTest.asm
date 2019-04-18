  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        1
  5         JUMPIF (1)   L11
  6         LOADL        0
  7         LOADL        0
  8         CALL         or      
  9         JUMP         L13
 10  L11:   LOADL        1
 11         JUMP         L13
 12  L12:   LOADL        0
 13         CALL         or      
 14  L13:   JUMPIF (1)   L14
 15         JUMP         L15
 16  L14:   LOADL        1
 17         CALL         putintnl
 18         POP          0
 19         JUMP         L16
 20  L15:   LOADL        1
 21         CALL         neg     
 22         CALL         putintnl
 23         POP          0
 24         JUMP         L16
 25  L16:   LOADL        0
 26         JUMPIF (0)   L17
 27         LOADL        1
 28         LOADL        1
 29         CALL         and     
 30         JUMP         L18
 31  L17:   LOADL        0
 32         JUMP         L18
 33  L18:   JUMPIF (1)   L19
 34         JUMP         L20
 35  L19:   LOADL        1
 36         CALL         neg     
 37         CALL         putintnl
 38         POP          0
 39         JUMP         L21
 40  L20:   LOADL        2
 41         CALL         putintnl
 42         POP          0
 43         JUMP         L21
 44  L21:   RETURN (0)   1
