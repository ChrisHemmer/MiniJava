  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        1
  5         LOAD         3[LB]
  6         CALL         putintnl
  7         LOADL        2
  8         LOAD         3[LB]
  9         CALL         mult    
 10         LOAD         3[LB]
 11         CALL         add     
 12         LOADL        1
 13         CALL         sub     
 14         STORE        3[LB]
 15         LOAD         3[LB]
 16         CALL         putintnl
 17         LOADL        3
 18         CALL         putintnl
 19         LOAD         3[LB]
 20         LOADL        1
 21         CALL         neg     
 22         CALL         ne      
 23         JUMPIF (1)   L11
 24         JUMP         L12
 25  L11:   LOADL        4
 26         CALL         putintnl
 27         JUMP         L13
 28  L12:   LOADL        1
 29         CALL         neg     
 30         CALL         putintnl
 31         JUMP         L13
 32  L13:   LOADL        0
 33         JUMP         L15
 34  L14:   LOAD         4[LB]
 35         LOADL        1
 36         CALL         add     
 37         STORE        4[LB]
 38         LOAD         4[LB]
 39         STORE        3[LB]
 40  L15:   LOAD         4[LB]
 41         LOADL        5
 42         CALL         lt      
 43         JUMPIF (1)   L14
 44         LOAD         3[LB]
 45         CALL         putintnl
 46         LOADL        75
 47         LOADL        50
 48         LOADL        25
 49         CALL         L18
 50         CALL         putintnl
 51         LOADL        500
 52         CALL         putintnl
 53         LOADL        1000
 54         LOADL        100
 55         CALL         L19
 56         JUMPIF (1)   L16
 57         JUMP         L17
 58  L16:   LOADL        3
 59         CALL         neg     
 60         CALL         putintnl
 61  L17:   RETURN (0)   1
 62  L18:   LOAD         -1[LB]
 63         LOAD         -2[LB]
 64         CALL         add     
 65         LOAD         -3[LB]
 66         CALL         add     
 67         RETURN (1)   3
 68  L19:   LOAD         -1[LB]
 69         LOAD         -2[LB]
 70         CALL         eq      
 71         RETURN (1)   2
