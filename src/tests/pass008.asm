  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        15
  5         CALL         newarr  
  6         LOADL        -1
  7         LOADL        3
  8         CALL         newobj  
  9         LOAD         4[LB]
 10         CALLI        L13
 11         LOADL        0
 12         JUMP         L12
 13  L11:   LOAD         3[LB]
 14         LOAD         5[LB]
 15         LOAD         4[LB]
 16         CALLI        L14
 17         CALL         arrayupd
 18         LOAD         5[LB]
 19         LOADL        1
 20         CALL         add     
 21         STORE        5[LB]
 22         POP          0
 23  L12:   LOAD         5[LB]
 24         LOAD         3[LB]
 25         CALL         arraylen
 26         CALL         lt      
 27         JUMPIF (1)   L11
 28         LOAD         3[LB]
 29         CALL         L16
 30         LOADL        100000
 31         CALL         putintnl
 32         LOAD         3[LB]
 33         CALL         arraylen
 34         LOADL        1
 35         CALL         sub     
 36         LOADL        0
 37         LOAD         3[LB]
 38         CALL         L24
 39         LOAD         3[LB]
 40         CALL         L16
 41         RETURN (0)   1
 42  L13:   LOADL        15342
 43         STORE        0[OB]
 44         LOADL        45192
 45         STORE        1[OB]
 46         LOADL        0
 47         STORE        2[OB]
 48         RETURN (0)   0
 49  L14:   LOAD         2[OB]
 50         LOAD         0[OB]
 51         CALL         mult    
 52         LOAD         1[OB]
 53         CALL         add     
 54         LOADL        100
 55         LOAD         3[LB]
 56         CALL         L15
 57         STORE        2[OB]
 58         LOAD         2[OB]
 59         RETURN (1)   0
 60  L15:   LOAD         -1[LB]
 61         LOAD         -1[LB]
 62         LOAD         -2[LB]
 63         CALL         div     
 64         LOAD         -2[LB]
 65         CALL         mult    
 66         CALL         sub     
 67         RETURN (1)   2
 68  L16:   LOADL        0
 69         JUMP         L18
 70  L17:   LOAD         -1[LB]
 71         LOAD         3[LB]
 72         CALL         arrayref
 73         CALL         putintnl
 74         LOAD         3[LB]
 75         LOADL        1
 76         CALL         add     
 77         STORE        3[LB]
 78         POP          0
 79  L18:   LOAD         3[LB]
 80         LOAD         -1[LB]
 81         CALL         arraylen
 82         CALL         lt      
 83         JUMPIF (1)   L17
 84         RETURN (0)   1
 85  L19:   LOAD         -1[LB]
 86         LOAD         -3[LB]
 87         CALL         arrayref
 88         LOAD         -2[LB]
 89         LOADL        1
 90         CALL         sub     
 91         LOAD         -2[LB]
 92         JUMP         L23
 93  L20:   LOAD         -1[LB]
 94         LOAD         5[LB]
 95         CALL         arrayref
 96         LOAD         3[LB]
 97         CALL         le      
 98         JUMPIF (1)   L21
 99         JUMP         L22
100  L21:   LOAD         4[LB]
101         LOADL        1
102         CALL         add     
103         STORE        4[LB]
104         LOAD         -1[LB]
105         LOAD         4[LB]
106         CALL         arrayref
107         LOAD         -1[LB]
108         LOAD         4[LB]
109         LOAD         -1[LB]
110         LOAD         5[LB]
111         CALL         arrayref
112         CALL         arrayupd
113         LOAD         -1[LB]
114         LOAD         5[LB]
115         LOAD         6[LB]
116         CALL         arrayupd
117         POP          1
118  L22:   LOAD         5[LB]
119         LOADL        1
120         CALL         add     
121         STORE        5[LB]
122         POP          0
123  L23:   LOAD         5[LB]
124         LOAD         -3[LB]
125         CALL         lt      
126         JUMPIF (1)   L20
127         LOAD         -1[LB]
128         LOAD         4[LB]
129         LOADL        1
130         CALL         add     
131         CALL         arrayref
132         LOAD         -1[LB]
133         LOAD         4[LB]
134         LOADL        1
135         CALL         add     
136         LOAD         -1[LB]
137         LOAD         -3[LB]
138         CALL         arrayref
139         CALL         arrayupd
140         LOAD         -1[LB]
141         LOAD         -3[LB]
142         LOAD         6[LB]
143         CALL         arrayupd
144         LOAD         4[LB]
145         LOADL        1
146         CALL         add     
147         RETURN (1)   3
148  L24:   LOAD         -2[LB]
149         LOAD         -3[LB]
150         CALL         lt      
151         JUMPIF (1)   L25
152         JUMP         L26
153  L25:   LOAD         -3[LB]
154         LOAD         -2[LB]
155         LOAD         -1[LB]
156         CALL         L19
157         LOAD         3[LB]
158         LOADL        1
159         CALL         sub     
160         LOAD         -2[LB]
161         LOAD         -1[LB]
162         CALL         L24
163         LOAD         -3[LB]
164         LOAD         3[LB]
165         LOADL        1
166         CALL         add     
167         LOAD         -1[LB]
168         CALL         L24
169         POP          1
170  L26:   RETURN (0)   3
