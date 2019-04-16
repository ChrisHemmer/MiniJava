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
 40         POP          0
 41  L15:   LOAD         4[LB]
 42         LOADL        5
 43         CALL         lt      
 44         JUMPIF (1)   L14
 45         LOAD         3[LB]
 46         CALL         putintnl
 47         LOADL        -1
 48         LOADL        2
 49         CALL         newobj  
 50         LOAD         5[LB]
 51         LOADL        0
 52         CALL         ne      
 53         JUMPIF (1)   L16
 54         JUMP         L17
 55  L16:   LOADL        6
 56         CALL         putintnl
 57  L17:   LOADL        7
 58         LOAD         5[LB]
 59         LOADL        1
 60         CALL         fieldref
 61         CALL         add     
 62         STORE        3[LB]
 63         LOAD         3[LB]
 64         CALL         putintnl
 65         LOAD         5[LB]
 66         LOADL        0
 67         LOADL        -1
 68         LOADL        2
 69         CALL         newobj  
 70         CALL         fieldupd
 71         LOAD         5[LB]
 72         LOADL        0
 73         CALL         fieldref
 74         LOADL        0
 75         LOADL        8
 76         CALL         fieldupd
 77         LOAD         5[LB]
 78         LOADL        0
 79         CALL         fieldref
 80         LOADL        0
 81         CALL         fieldref
 82         CALL         putintnl
 83         LOADL        4
 84         CALL         newarr  
 85         LOAD         6[LB]
 86         CALL         arraylen
 87         STORE        3[LB]
 88         LOADL        2
 89         LOAD         3[LB]
 90         CALL         mult    
 91         LOADL        1
 92         CALL         add     
 93         CALL         putintnl
 94         LOAD         6[LB]
 95         LOADL        0
 96         LOADL        0
 97         CALL         arrayupd
 98         LOADL        1
 99         STORE        4[LB]
100         JUMP         L19
101  L18:   LOAD         6[LB]
102         LOAD         4[LB]
103         LOAD         6[LB]
104         LOAD         4[LB]
105         LOADL        1
106         CALL         sub     
107         CALL         arrayref
108         LOAD         4[LB]
109         CALL         add     
110         CALL         arrayupd
111         LOAD         4[LB]
112         LOADL        1
113         CALL         add     
114         STORE        4[LB]
115         POP          0
116  L19:   LOAD         4[LB]
117         LOAD         6[LB]
118         CALL         arraylen
119         CALL         lt      
120         JUMPIF (1)   L18
121         LOAD         6[LB]
122         LOADL        3
123         CALL         arrayref
124         LOADL        4
125         CALL         add     
126         STORE        3[LB]
127         LOAD         3[LB]
128         CALL         putintnl
129         LOAD         5[LB]
130         CALLI        L20
131         LOADL        999
132         CALL         putintnl
133         RETURN (0)   1
134  L20:   LOADL        11
135         LOAD         3[LB]
136         CALL         putintnl
137         LOAD         0[OB]
138         LOADL        1
139         LOADA        0[OB]
140         CALL         fieldupd
141         LOADL        12
142         STORE        1[OB]
143         LOAD         0[OB]
144         LOADL        1
145         CALL         fieldref
146         LOADL        1
147         CALL         fieldref
148         STORE        3[LB]
149         LOAD         3[LB]
150         CALL         putintnl
151         LOADL        4
152         STORE        1[OB]
153         LOADL        2
154         LOADL        4
155         LOADL        3
156         LOADA        0[OB]
157         CALLI        L21
158         CALL         add     
159         STORE        3[LB]
160         LOAD         3[LB]
161         CALL         putintnl
162         LOADL        8
163         LOADL        3
164         LOAD         0[OB]
165         CALLI        L23
166         CALL         add     
167         CALL         putintnl
168         LOADA        0[OB]
169         LOADL        1
170         LOADL        4
171         CALL         fieldupd
172         LOAD         0[OB]
173         LOADL        0
174         LOADL        5
175         CALL         fieldupd
176         LOADL        2
177         LOADA        0[OB]
178         LOADL        0
179         CALL         fieldref
180         LOADA        0[OB]
181         LOADA        0[OB]
182         CALLI        L22
183         CALL         add     
184         CALL         putintnl
185         RETURN (0)   0
186  L21:   LOAD         1[OB]
187         LOAD         -1[LB]
188         CALL         add     
189         LOAD         -2[LB]
190         CALL         add     
191         RETURN (1)   2
192  L22:   LOAD         -1[LB]
193         LOADL        1
194         CALL         fieldref
195         LOAD         -2[LB]
196         LOADL        0
197         CALL         fieldref
198         CALL         add     
199         LOADA        0[OB]
200         LOADL        1
201         CALL         fieldref
202         CALL         add     
203         RETURN (1)   2
204  L23:   LOADL        1
205         LOAD         -1[LB]
206         LOADL        1
207         CALL         gt      
208         JUMPIF (1)   L24
209         JUMP         L25
210  L24:   LOAD         -1[LB]
211         LOAD         -1[LB]
212         LOADL        1
213         CALL         sub     
214         LOADA        0[OB]
215         CALLI        L23
216         CALL         mult    
217         STORE        3[LB]
218  L25:   LOAD         3[LB]
219         RETURN (1)   1
