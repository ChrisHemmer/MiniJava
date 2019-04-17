  0         PUSH         1
  1         LOADL        0
  2         STORE        0[SB]
  3         PUSH         1
  4         LOADL        0
  5         STORE        1[SB]
  6         LOADL        0
  7         CALL         newarr  
  8         CALL         L13
  9         HALT   (0)   
 10  L10:   LOAD         -1[LB]
 11         LOADL        0
 12         CALL         eq      
 13         JUMPIF (1)   L11
 14         JUMP         L12
 15  L11:   LOAD         1[SB]
 16         CALL         putintnl
 17         POP          0
 18  L12:   LOAD         0[OB]
 19         LOAD         -1[LB]
 20         CALL         add     
 21         STORE        0[OB]
 22         LOAD         1[SB]
 23         LOAD         -1[LB]
 24         CALL         add     
 25         STORE        1[SB]
 26         RETURN (0)   1
 27  L13:   LOADL        -1
 28         LOADL        1
 29         CALL         newobj  
 30         LOADL        15
 31         LOAD         3[LB]
 32         CALLI        L10
 33         LOADL        -1
 34         LOADL        1
 35         CALL         newobj  
 36         LOADL        12
 37         LOAD         4[LB]
 38         CALLI        L10
 39         LOAD         1[SB]
 40         CALL         putintnl
 41         LOAD         1[SB]
 42         LOADL        2
 43         CALL         mult    
 44         LOADL        4
 45         CALL         add     
 46         STORE        1[SB]
 47         LOAD         1[SB]
 48         CALL         putintnl
 49         LOADL        -1
 50         LOADL        1
 51         CALL         newobj  
 52         STORE        0[SB]
 53         LOAD         1[SB]
 54         CALL         putintnl
 55         LOADL        12
 56         LOAD         0[SB]
 57         CALLI        L10
 58         LOADL        0
 59         CALL         putintnl
 60         LOADA        0[SB]
 61         LOADL        0
 62         CALL         fieldref
 63         LOADL        0
 64         CALL         fieldref
 65         CALL         putintnl
 66         RETURN (0)   1
