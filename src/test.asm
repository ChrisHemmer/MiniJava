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
 59         LOADL        0
 60         CALL         fieldref
 61         CALL         add     
 62         STORE        3[LB]
 63         LOAD         3[LB]
 64         CALL         putintnl
 65         LOAD         5[LB]
 66         LOADL        0
 67         LOADL        1573
 68         CALL         fieldupd
 69         LOAD         5[LB]
 70         LOADL        1
 71         LOADL        -1
 72         LOADL        2
 73         CALL         newobj  
 74         CALL         fieldupd
 75         LOAD         5[LB]
 76         LOADL        1
 77         CALL         fieldref
 78         LOADL        0
 79         LOADL        8
 80         CALL         fieldupd
 81         LOAD         5[LB]
 82         LOADL        1
 83         CALL         fieldref
 84         LOADL        0
 85         CALL         fieldref
 86         CALL         putintnl
 87         LOADL        4
 88         CALL         newarr  
 89         LOAD         6[LB]
 90         CALL         arraylen
 91         STORE        3[LB]
 92         LOADL        2
 93         LOAD         3[LB]
 94         CALL         mult    
 95         LOADL        1
 96         CALL         add     
 97         CALL         putintnl
 98         LOAD         6[LB]
 99         LOADL        0
100         LOADL        0
101         CALL         arrayupd
102         LOADL        1
103         STORE        4[LB]
104         JUMP         L19
105  L18:   LOAD         6[LB]
106         LOAD         4[LB]
107         LOAD         6[LB]
108         LOAD         4[LB]
109         LOADL        1
110         CALL         sub     
111         CALL         arrayref
112         LOAD         4[LB]
113         CALL         add     
114         CALL         arrayupd
115         LOAD         4[LB]
116         LOADL        1
117         CALL         add     
118         STORE        4[LB]
119         POP          0
120  L19:   LOAD         4[LB]
121         LOAD         6[LB]
122         CALL         arraylen
123         CALL         lt      
124         JUMPIF (1)   L18
125         LOAD         6[LB]
126         LOADL        3
127         CALL         arrayref
128         LOADL        4
129         CALL         add     
130         STORE        3[LB]
131         LOAD         3[LB]
132         CALL         putintnl
133         LOADL        1
134         CALL         putintnl
135         LOADL        2
136         CALL         putintnl
137         LOADL        3
138         CALL         putintnl
139         LOAD         5[LB]
140         CALLI        L20
141         LOADL        999
142         CALL         putintnl
143         RETURN (0)   1
144  L20:   LOADL        11
145         LOAD         3[LB]
146         CALL         putintnl
147         LOADL        4
148         CALL         putintnl
149         LOADL        200
150         CALL         putintnl
151         LOADL        12
152         STORE        0[OB]
153         LOAD         1[LB]
154         LOADL        1
155         CALL         fieldref
156         LOADL        0
157         CALL         fieldref
158         STORE        3[LB]
159         LOAD         3[LB]
160         CALL         putintnl
161         LOADL        4
162         STORE        0[OB]
163         LOADL        2
164         LOADL        4
165         LOADL        3
166         CALL         L21
167         CALL         add     
168         STORE        3[LB]
169         LOAD         3[LB]
170         CALL         putintnl
171         LOADL        8
172         LOADL        3
173         CALL         L23
174         CALL         add     
175         CALL         putintnl
176         LOAD         0[OB]
177         LOADL        4
178         CALL         fieldupd
179         LOAD         1[LB]
180         LOADL        0
181         LOADL        5
182         CALL         fieldupd
183         LOADL        2
184         LOAD         1[OB]
185         LOAD         1[OB]
186         LOAD         0[OB]
187         CALL         L22
188         CALL         add     
189         CALL         putintnl
190         RETURN (0)   0
191  L21:   LOAD         0[OB]
192         LOAD         -1[LB]
193         CALL         add     
194         LOAD         -2[LB]
195         CALL         add     
196         RETURN (1)   2
197  L22:   LOAD         -1[LB]
198         LOADL        0
199         CALL         fieldref
200         LOAD         -2[LB]
201         LOADL        0
202         CALL         fieldref
203         CALL         add     
204         LOAD         0[OB]
205         LOAD         0[OB]
206         CALL         add     
207         RETURN (1)   2
208  L23:   LOADL        1
209         LOAD         -1[LB]
210         LOADL        1
211         CALL         gt      
212         JUMPIF (1)   L24
213         JUMP         L25
214  L24:   LOAD         -1[LB]
215         LOAD         -1[LB]
216         LOADL        1
217         CALL         sub     
218         CALL         L23
219         CALL         mult    
220         STORE        3[LB]
221  L25:   LOAD         3[LB]
222         RETURN (1)   1
