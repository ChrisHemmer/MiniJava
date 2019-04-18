  0         PUSH         1
  1         LOADL        0
  2         STORE        0[SB]
  3         LOADL        0
  4         CALL         newarr  
  5         CALL         L10
  6         HALT   (0)   
  7  L10:   LOADL        -1
  8         LOADL        2
  9         CALL         newobj  
 10         LOAD         3[LB]
 11         LOADL        0
 12         LOADL        0
 13         CALL         fieldupd
 14         LOAD         3[LB]
 15         LOADL        1
 16         LOADL        100
 17         CALL         fieldupd
 18         LOADL        5
 19         CALL         newarr  
 20         STORE        0[SB]
 21         LOAD         0[SB]
 22         LOADL        0
 23         LOADL        13
 24         CALL         arrayupd
 25         LOAD         0[SB]
 26         LOADL        1
 27         LOADL        33
 28         CALL         arrayupd
 29         LOAD         0[SB]
 30         LOADL        2
 31         LOADL        53
 32         CALL         arrayupd
 33         LOAD         0[SB]
 34         LOADL        3
 35         LOADL        73
 36         CALL         arrayupd
 37         LOAD         0[SB]
 38         LOADL        4
 39         LOADL        93
 40         CALL         arrayupd
 41         LOAD         3[LB]
 42         CALLI        L13
 43         LOADL        95
 44         CALL         newarr  
 45         STORE        0[SB]
 46         LOADL        0
 47         JUMP         L12
 48  L11:   LOAD         0[SB]
 49         LOAD         4[LB]
 50         LOAD         4[LB]
 51         CALL         arrayupd
 52         LOAD         4[LB]
 53         LOADL        1
 54         CALL         add     
 55         STORE        4[LB]
 56         POP          0
 57  L12:   LOAD         4[LB]
 58         LOAD         0[SB]
 59         CALL         arraylen
 60         CALL         lt      
 61         JUMPIF (1)   L11
 62         LOADL        100000
 63         CALL         putintnl
 64         LOAD         3[LB]
 65         CALLI        L13
 66         RETURN (0)   1
 67  L13:   LOADL        0
 68         JUMP         L19
 69  L14:   LOADL        0
 70         JUMP         L18
 71  L15:   LOAD         3[LB]
 72         LOADL        10
 73         CALL         mult    
 74         LOAD         4[LB]
 75         CALL         add     
 76         LOAD         5[LB]
 77         LOAD         0[SB]
 78         CALL         L20
 79         CALL         not     
 80         JUMPIF (1)   L16
 81         JUMP         L17
 82  L16:   LOAD         5[LB]
 83         CALL         putintnl
 84         POP          0
 85  L17:   LOAD         4[LB]
 86         LOADL        1
 87         CALL         add     
 88         STORE        4[LB]
 89         POP          1
 90  L18:   LOAD         4[LB]
 91         LOADL        10
 92         CALL         lt      
 93         JUMPIF (1)   L15
 94         LOAD         3[LB]
 95         LOADL        1
 96         CALL         add     
 97         STORE        3[LB]
 98         POP          1
 99  L19:   LOAD         3[LB]
100         LOAD         1[OB]
101         LOADL        10
102         CALL         div     
103         CALL         lt      
104         JUMPIF (1)   L14
105         RETURN (0)   0
106  L20:   LOADL        0
107         JUMP         L24
108  L21:   LOAD         -1[LB]
109         LOAD         3[LB]
110         CALL         arrayref
111         LOAD         -2[LB]
112         CALL         eq      
113         JUMPIF (1)   L22
114         JUMP         L23
115  L22:   LOADL        1
116         RETURN (1)   2
117         POP          0
118  L23:   LOAD         3[LB]
119         LOADL        1
120         CALL         add     
121         STORE        3[LB]
122         POP          0
123  L24:   LOAD         3[LB]
124         LOAD         -1[LB]
125         CALL         arraylen
126         CALL         lt      
127         JUMPIF (1)   L21
128         LOADL        0
129         RETURN (1)   2
