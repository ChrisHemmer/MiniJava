  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        -1
  5         LOADL        1
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         LOADL        0
  9         LOADL        -1
 10         LOADL        2
 11         CALL         newobj  
 12         CALL         fieldupd
 13         LOAD         3[LB]
 14         LOADL        0
 15         CALL         fieldref
 16         LOADL        0
 17         LOADL        -1
 18         LOADL        1
 19         CALL         newobj  
 20         CALL         fieldupd
 21         LOAD         3[LB]
 22         LOADL        0
 23         CALL         fieldref
 24         LOADL        0
 25         CALL         fieldref
 26         LOADL        0
 27         CALL         fieldref
 28         CALL         putintnl
 29         LOAD         3[LB]
 30         LOADL        0
 31         CALL         fieldref
 32         LOADL        0
 33         CALL         fieldref
 34         LOAD         3[LB]
 35         LOADL        0
 36         CALL         fieldref
 37         LOADL        0
 38         CALL         fieldref
 39         LOADL        0
 40         LOADL        1
 41         CALL         fieldupd
 42         LOAD         4[LB]
 43         LOADL        0
 44         CALL         fieldref
 45         CALL         putintnl
 46         LOAD         4[LB]
 47         LOADL        0
 48         LOADL        2
 49         CALL         fieldupd
 50         LOAD         3[LB]
 51         LOADL        0
 52         CALL         fieldref
 53         LOADL        0
 54         CALL         fieldref
 55         LOADL        0
 56         CALL         fieldref
 57         CALL         putintnl
 58         CALL         L21
 59         JUMPIF (1)   L11
 60         JUMP         L12
 61  L11:   LOADL        -1
 62         LOADL        2
 63         CALL         newobj  
 64         LOAD         5[LB]
 65         LOADL        1
 66         LOADL        4
 67         CALL         fieldupd
 68         LOAD         5[LB]
 69         CALL         L22
 70         CALL         putintnl
 71         POP          1
 72  L12:   LOADL        5
 73         CALL         L13
 74         LOADL        17
 75         CALL         L18
 76         RETURN (0)   1
 77  L13:   LOAD         -1[LB]
 78         LOADL        15
 79         CALL         gt      
 80         JUMPIF (1)   L14
 81         JUMP         L15
 82  L14:   RETURN (0)   1
 83         POP          0
 84  L15:   LOAD         -1[LB]
 85         JUMP         L17
 86  L16:   LOAD         3[LB]
 87         CALL         putintnl
 88         LOAD         3[LB]
 89         LOADL        1
 90         CALL         add     
 91         STORE        3[LB]
 92         POP          0
 93  L17:   LOAD         3[LB]
 94         LOAD         -1[LB]
 95         LOADL        3
 96         CALL         add     
 97         CALL         lt      
 98         JUMPIF (1)   L16
 99         LOAD         3[LB]
100         CALL         L13
101         RETURN (0)   1
102  L18:   LOAD         -1[LB]
103         CALL         putintnl
104         LOAD         -1[LB]
105         LOADL        250
106         CALL         ne      
107         JUMPIF (1)   L19
108         JUMP         L20
109  L19:   LOAD         -1[LB]
110         LOADL        1
111         CALL         add     
112         CALL         L18
113         POP          0
114  L20:   RETURN (0)   1
115  L21:   LOADL        3
116         CALL         putintnl
117         LOADL        1
118         RETURN (1)   0
119  L22:   LOAD         -1[LB]
120         LOADL        1
121         CALL         fieldref
122         RETURN (1)   1
