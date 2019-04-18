  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        -1
  5         LOADL        1
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         LOADL        0
  9         CALL         fieldref
 10         LOADL        0
 11         CALL         eq      
 12         JUMPIF (1)   L11
 13         JUMP         L12
 14  L11:   LOADL        1
 15         CALL         putintnl
 16         POP          0
 17  L12:   LOADL        5
 18         LOADL        5
 19         CALL         eq      
 20         JUMPIF (0)   L13
 21         LOADL        1
 22         LOADL        0
 23         CALL         and     
 24         JUMP         L14
 25  L13:   LOADL        0
 26         JUMP         L14
 27  L14:   JUMPIF (1)   L15
 28         JUMP         L16
 29  L15:   LOADL        1
 30         CALL         neg     
 31         CALL         putintnl
 32         POP          0
 33         JUMP         L17
 34  L16:   LOADL        2
 35         CALL         putintnl
 36         POP          0
 37         JUMP         L17
 38  L17:   LOADL        5
 39         LOADL        2
 40         CALL         lt      
 41         JUMPIF (0)   L18
 42         LOADL        1
 43         LOADL        1
 44         CALL         and     
 45         JUMP         L19
 46  L18:   LOADL        0
 47         JUMP         L19
 48  L19:   JUMPIF (1)   L20
 49         JUMP         L21
 50  L20:   LOADL        1
 51         CALL         neg     
 52         CALL         putintnl
 53         POP          0
 54         JUMP         L22
 55  L21:   LOADL        3
 56         CALL         putintnl
 57         POP          0
 58         JUMP         L22
 59  L22:   LOADL        0
 60         JUMPIF (0)   L23
 61         LOADL        1
 62         LOADL        0
 63         CALL         and     
 64         JUMP         L24
 65  L23:   LOADL        0
 66         JUMP         L24
 67  L24:   JUMPIF (1)   L25
 68         JUMP         L26
 69  L25:   LOADL        1
 70         CALL         neg     
 71         CALL         putintnl
 72         POP          0
 73         JUMP         L27
 74  L26:   LOADL        4
 75         CALL         putintnl
 76         POP          0
 77         JUMP         L27
 78  L27:   LOADL        1
 79         JUMPIF (0)   L28
 80         LOADL        1
 81         LOADL        5
 82         LOADL        6
 83         CALL         mult    
 84         LOADL        30
 85         CALL         eq      
 86         CALL         and     
 87         JUMP         L29
 88  L28:   LOADL        0
 89         JUMP         L29
 90  L29:   JUMPIF (1)   L30
 91         JUMP         L31
 92  L30:   LOADL        5
 93         CALL         putintnl
 94         POP          0
 95         JUMP         L32
 96  L31:   LOADL        1
 97         CALL         neg     
 98         CALL         putintnl
 99         POP          0
100         JUMP         L32
101  L32:   LOADL        0
102         JUMPIF (0)   L33
103         LOADL        1
104         LOAD         3[LB]
105         LOADL        0
106         CALL         fieldref
107         LOADL        0
108         CALL         fieldref
109         LOADL        5
110         CALL         neg     
111         CALL         eq      
112         CALL         and     
113         JUMP         L34
114  L33:   LOADL        0
115         JUMP         L34
116  L34:   JUMPIF (1)   L35
117         JUMP         L36
118  L35:   LOADL        1
119         CALL         neg     
120         CALL         putintnl
121         POP          0
122         JUMP         L37
123  L36:   LOADL        6
124         CALL         putintnl
125         POP          0
126         JUMP         L37
127  L37:   LOADL        1
128         JUMPIF (1)   L38
129         LOADL        0
130         LOADL        0
131         CALL         or      
132         JUMP         L40
133  L38:   LOADL        1
134         JUMP         L40
135  L39:   LOADL        0
136         CALL         or      
137  L40:   JUMPIF (1)   L41
138         JUMP         L42
139  L41:   LOADL        7
140         CALL         putintnl
141         POP          0
142         JUMP         L43
143  L42:   LOADL        1
144         CALL         neg     
145         CALL         putintnl
146         POP          0
147         JUMP         L43
148  L43:   LOADL        5
149         LOADL        4
150         CALL         eq      
151         JUMPIF (1)   L44
152         LOADL        0
153         LOADL        1
154         CALL         or      
155         JUMP         L46
156  L44:   LOADL        1
157         JUMP         L46
158  L45:   LOADL        1
159         CALL         or      
160  L46:   JUMPIF (1)   L47
161         JUMP         L48
162  L47:   LOADL        8
163         CALL         putintnl
164         POP          0
165         JUMP         L49
166  L48:   LOADL        1
167         CALL         neg     
168         CALL         putintnl
169         POP          0
170         JUMP         L49
171  L49:   LOADL        0
172         JUMPIF (1)   L50
173         LOADL        0
174         LOADL        0
175         CALL         or      
176         JUMP         L52
177  L50:   LOADL        1
178         JUMP         L52
179  L51:   LOADL        0
180         CALL         or      
181  L52:   JUMPIF (1)   L53
182         JUMP         L54
183  L53:   LOADL        1
184         CALL         neg     
185         CALL         putintnl
186         POP          0
187         JUMP         L55
188  L54:   LOADL        9
189         CALL         putintnl
190         POP          0
191         JUMP         L55
192  L55:   LOADL        1
193         JUMPIF (1)   L56
194         LOADL        0
195         LOADL        1
196         CALL         or      
197         JUMP         L58
198  L56:   LOADL        1
199         JUMP         L58
200  L57:   LOADL        1
201         CALL         or      
202  L58:   JUMPIF (1)   L59
203         JUMP         L60
204  L59:   LOADL        10
205         CALL         putintnl
206         POP          0
207         JUMP         L61
208  L60:   LOADL        1
209         CALL         neg     
210         CALL         putintnl
211         POP          0
212         JUMP         L61
213  L61:   LOADL        1
214         JUMPIF (1)   L62
215         LOADL        0
216         LOAD         3[LB]
217         LOADL        0
218         CALL         fieldref
219         LOADL        0
220         CALL         fieldref
221         LOADL        5
222         CALL         eq      
223         CALL         or      
224         JUMP         L64
225  L62:   LOADL        1
226         JUMP         L64
227  L63:   LOAD         3[LB]
228         LOADL        0
229         CALL         fieldref
230         LOADL        0
231         CALL         fieldref
232         LOADL        5
233         CALL         eq      
234         CALL         or      
235  L64:   JUMPIF (1)   L65
236         JUMP         L66
237  L65:   LOADL        11
238         CALL         putintnl
239         POP          0
240         JUMP         L67
241  L66:   LOADL        1
242         CALL         neg     
243         CALL         putintnl
244         POP          0
245         JUMP         L67
246  L67:   LOADL        0
247         JUMPIF (0)   L68
248         LOADL        1
249         LOAD         3[LB]
250         LOADL        0
251         CALL         fieldref
252         LOADL        0
253         CALL         fieldref
254         LOADL        5
255         CALL         eq      
256         CALL         and     
257         JUMP         L69
258  L68:   LOADL        0
259         JUMP         L69
260  L69:   LOADL        0
261         JUMPIF (0)   L70
262         LOADL        1
263         LOAD         3[LB]
264         LOADL        0
265         CALL         fieldref
266         LOADL        0
267         CALL         fieldref
268         LOADL        5
269         CALL         eq      
270         CALL         and     
271         JUMP         L71
272  L70:   LOADL        0
273         JUMP         L71
274  L71:   JUMPIF (1)   L72
275         LOADL        0
276         LOADL        1
277         CALL         or      
278         JUMP         L74
279  L72:   LOADL        1
280         JUMP         L74
281  L73:   LOADL        1
282         CALL         or      
283  L74:   JUMPIF (1)   L75
284         LOADL        0
285         LOAD         3[LB]
286         LOADL        0
287         CALL         fieldref
288         LOADL        0
289         CALL         fieldref
290         LOADL        10
291         CALL         eq      
292         CALL         or      
293         JUMP         L77
294  L75:   LOADL        1
295         JUMP         L77
296  L76:   LOAD         3[LB]
297         LOADL        0
298         CALL         fieldref
299         LOADL        0
300         CALL         fieldref
301         LOADL        10
302         CALL         eq      
303         CALL         or      
304  L77:   JUMPIF (0)   L78
305         LOADL        1
306         LOADL        1
307         CALL         and     
308         JUMP         L79
309  L78:   LOADL        0
310         JUMP         L79
311  L79:   JUMPIF (1)   L80
312         JUMP         L81
313  L80:   LOADL        12
314         CALL         putintnl
315         POP          0
316         JUMP         L82
317  L81:   LOADL        1
318         CALL         neg     
319         CALL         putintnl
320         POP          0
321         JUMP         L82
322  L82:   RETURN (0)   1
