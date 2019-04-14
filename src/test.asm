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
 46         LOADL        -1
 47         LOADL        1
 48         CALL         newobj  
 49         LOAD         5[LB]
 50         LOADL        0
 51         CALL         ne      
 52         JUMPIF (1)   L16
 53         JUMP         L17
 54  L16:   LOADL        6
 55         CALL         putintnl
 56  L17:   LOADL        0
 57         CALL         putintnl
 58         LOADL        0
 59         CALL         putintnl
 60         LOADL        0
 61         CALL         putintnl
 62         LOADL        7
 63         LOAD         5[LB]
 64         LOADL        0
 65         CALL         fieldref
 66         CALL         add     
 67         CALL         putintnl
 68         LOADL        8
 69         STORE        0[LB]
 70         LOAD         5[LB]
 71         LOADL        0
 72         CALL         fieldref
 73         CALL         putintnl
 74         RETURN (0)   1
 75  L18:   LOAD         -1[LB]
 76         LOADL        1
 77         CALL         neg     
 78         CALL         eq      
 79         JUMPIF (1)   L19
 80         JUMP         L20
 81  L19:   RETURN (0)   1
 82         JUMP         L21
 83  L20:   LOAD         -1[LB]
 84         CALL         putintnl
 85         LOAD         -1[LB]
 86         LOADL        1
 87         CALL         sub     
 88         CALL         L18
 89         JUMP         L21
 90  L21:   RETURN (0)   1
 91         LOAD         -1[LB]
 92         LOAD         -2[LB]
 93         CALL         add     
 94         LOAD         -3[LB]
 95         CALL         add     
 96         RETURN (1)   3
 97         LOAD         -1[LB]
 98         LOAD         -2[LB]
 99         CALL         eq      
100         RETURN (1)   2
101         LOADL        0
102         JUMP         L23
103  L22:   LOADL        0
104         CALL         putintnl
105         LOAD         3[LB]
106         LOADL        1
107         CALL         add     
108         STORE        3[LB]
109  L23:   LOAD         3[LB]
110         LOADL        5
111         CALL         lt      
112         JUMPIF (1)   L22
113         RETURN (0)   0
114         LOADL        15
115         STORE        0[LB]
116         LOAD         0[LB]
117         CALL         putintnl
118         RETURN (0)   0
119         LOAD         -1[LB]
120         STORE        0[LB]
121         RETURN (0)   1
