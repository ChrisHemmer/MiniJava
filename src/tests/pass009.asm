  0         PUSH         1
  1         LOADL        0
  2         STORE        0[SB]
  3         LOADL        0
  4         CALL         newarr  
  5         CALL         L10
  6         HALT   (0)   
  7  L10:   LOADL        1
  8         LOAD         3[LB]
  9         CALL         putintnl
 10         LOADL        2
 11         LOAD         3[LB]
 12         CALL         mult    
 13         LOAD         3[LB]
 14         CALL         add     
 15         LOADL        1
 16         CALL         sub     
 17         STORE        3[LB]
 18         LOAD         3[LB]
 19         CALL         putintnl
 20         LOADL        3
 21         CALL         putintnl
 22         LOAD         3[LB]
 23         LOADL        1
 24         CALL         neg     
 25         CALL         ne      
 26         JUMPIF (1)   L11
 27         JUMP         L12
 28  L11:   LOADL        4
 29         CALL         putintnl
 30         JUMP         L13
 31  L12:   LOADL        1
 32         CALL         neg     
 33         CALL         putintnl
 34         JUMP         L13
 35  L13:   LOADL        0
 36         JUMP         L15
 37  L14:   LOAD         4[LB]
 38         LOADL        1
 39         CALL         add     
 40         STORE        4[LB]
 41         LOAD         4[LB]
 42         STORE        3[LB]
 43         POP          0
 44  L15:   LOAD         4[LB]
 45         LOADL        5
 46         CALL         lt      
 47         JUMPIF (1)   L14
 48         LOAD         3[LB]
 49         CALL         putintnl
 50         LOADL        -1
 51         LOADL        2
 52         CALL         newobj  
 53         LOAD         5[LB]
 54         LOADL        0
 55         CALL         ne      
 56         JUMPIF (1)   L16
 57         JUMP         L17
 58  L16:   LOADL        6
 59         CALL         putintnl
 60  L17:   LOADL        7
 61         LOAD         5[LB]
 62         LOADL        0
 63         CALL         fieldref
 64         CALL         add     
 65         STORE        3[LB]
 66         LOAD         3[LB]
 67         CALL         putintnl
 68         LOAD         5[LB]
 69         LOADL        1
 70         LOADL        -1
 71         LOADL        2
 72         CALL         newobj  
 73         CALL         fieldupd
 74         LOAD         5[LB]
 75         LOADL        1
 76         CALL         fieldref
 77         LOADL        0
 78         LOADL        8
 79         CALL         fieldupd
 80         LOAD         5[LB]
 81         LOADL        1
 82         CALL         fieldref
 83         LOADL        0
 84         CALL         fieldref
 85         CALL         putintnl
 86         LOADL        4
 87         CALL         newarr  
 88         LOAD         6[LB]
 89         CALL         arraylen
 90         STORE        3[LB]
 91         LOADL        2
 92         LOAD         3[LB]
 93         CALL         mult    
 94         LOADL        1
 95         CALL         add     
 96         CALL         putintnl
 97         LOAD         6[LB]
 98         LOADL        0
 99         LOADL        0
100         CALL         arrayupd
101         LOADL        1
102         STORE        4[LB]
103         JUMP         L19
104  L18:   LOAD         6[LB]
105         LOAD         4[LB]
106         LOAD         6[LB]
107         LOAD         4[LB]
108         LOADL        1
109         CALL         sub     
110         CALL         arrayref
111         LOAD         4[LB]
112         CALL         add     
113         CALL         arrayupd
114         LOAD         4[LB]
115         LOADL        1
116         CALL         add     
117         STORE        4[LB]
118         POP          0
119  L19:   LOAD         4[LB]
120         LOAD         6[LB]
121         CALL         arraylen
122         CALL         lt      
123         JUMPIF (1)   L18
124         LOAD         6[LB]
125         LOADL        3
126         CALL         arrayref
127         LOADL        4
128         CALL         add     
129         STORE        3[LB]
130         LOAD         3[LB]
131         CALL         putintnl
132         LOAD         5[LB]
133         CALLI        L45
134         LOADL        2
135         LOADL        4
136         CALL         L53
137         CALL         putintnl
138         LOADL        0
139         STORE        0[SB]
140         LOADL        2
141         CALL         L56
142         CALL         L77
143         LOADL        1
144         LOAD         7[LB]
145         JUMPIF (1)   L20
146         JUMP         L21
147  L20:   LOADL        18
148         CALL         putintnl
149  L21:   LOAD         7[LB]
150         CALL         not     
151         JUMPIF (1)   L22
152         JUMP         L23
153  L22:   LOADL        1
154         CALL         neg     
155         CALL         putintnl
156         POP          0
157         JUMP         L24
158  L23:   LOADL        19
159         CALL         putintnl
160         POP          0
161         JUMP         L24
162  L24:   LOADL        0
163         LOADL        3
164         LOADL        4
165         CALL         lt      
166         JUMPIF (1)   L25
167         LOADL        0
168         LOAD         8[LB]
169         LOADL        0
170         CALL         fieldref
171         CALL         or      
172         JUMP         L27
173  L25:   LOADL        1
174         JUMP         L27
175  L26:   LOAD         8[LB]
176         LOADL        0
177         CALL         fieldref
178         CALL         or      
179  L27:   JUMPIF (1)   L28
180         JUMP         L29
181  L28:   LOADL        20
182         CALL         putintnl
183         POP          0
184  L29:   LOADL        3
185         LOADL        4
186         CALL         gt      
187         JUMPIF (0)   L30
188         LOADL        1
189         LOAD         8[LB]
190         LOADL        0
191         CALL         fieldref
192         CALL         and     
193         JUMP         L31
194  L30:   LOADL        0
195         JUMP         L31
196  L31:   JUMPIF (1)   L32
197         JUMP         L33
198  L32:   LOADL        1
199         CALL         neg     
200         CALL         putintnl
201         POP          0
202         JUMP         L34
203  L33:   LOADL        21
204         CALL         putintnl
205         POP          0
206         JUMP         L34
207  L34:   LOADL        8
208         CALL         newarr  
209         LOAD         9[LB]
210         LOADL        0
211         LOADL        1
212         CALL         arrayupd
213         LOAD         9[LB]
214         LOADL        1
215         LOADL        5
216         CALL         arrayupd
217         LOAD         9[LB]
218         LOADL        2
219         LOADL        8
220         CALL         arrayupd
221         LOAD         9[LB]
222         LOADL        3
223         LOADL        9
224         CALL         arrayupd
225         LOAD         9[LB]
226         LOADL        4
227         LOADL        10
228         CALL         arrayupd
229         LOAD         9[LB]
230         LOADL        5
231         LOADL        17
232         CALL         arrayupd
233         LOAD         9[LB]
234         LOADL        6
235         LOADL        17
236         CALL         arrayupd
237         LOAD         9[LB]
238         LOADL        7
239         LOADL        20
240         CALL         arrayupd
241         LOAD         9[LB]
242         CALL         arraylen
243         LOAD         10[LB]
244         LOAD         9[LB]
245         CALL         L85
246         CALL         putintnl
247         LOADL        23
248         LOAD         11[LB]
249         CALL         putintnl
250         LOADL        3
251         STORE        0[SB]
252         LOADL        2
253         CALL         L56
254         STORE        3[LB]
255         LOAD         3[LB]
256         CALL         putintnl
257         LOADL        -1
258         LOADL        2
259         CALL         newobj  
260         LOAD         12[LB]
261         LOADL        0
262         LOADL        1
263         CALL         neg     
264         CALL         fieldupd
265         LOAD         12[LB]
266         CALL         L49
267         LOAD         12[LB]
268         LOADL        0
269         CALL         fieldref
270         CALL         putintnl
271         LOAD         12[LB]
272         LOADL        0
273         LOADL        26
274         CALL         fieldupd
275         LOAD         12[LB]
276         LOADL        0
277         CALL         fieldref
278         LOAD         12[LB]
279         CALLI        L48
280         LOAD         12[LB]
281         LOADL        0
282         CALL         fieldref
283         CALL         putintnl
284         LOADL        27
285         LOADL        27
286         LOADL        7
287         CALL         mult    
288         LOAD         14[LB]
289         LOAD         13[LB]
290         CALL         L68
291         CALL         putintnl
292         LOADL        -1
293         LOADL        1
294         CALL         newobj  
295         LOADL        28
296         LOAD         15[LB]
297         CALLI        L90
298         LOAD         15[LB]
299         CALLI        L91
300         CALL         putintnl
301         LOADL        0
302         LOADL        0
303         CALL         eq      
304         JUMPIF (1)   L35
305         JUMP         L36
306  L35:   LOADL        29
307         CALL         putintnl
308         POP          0
309  L36:   CALL         L40
310         LOADL        30
311         CALL         putintnl
312         LOADL        -1
313         LOADL        2
314         CALL         newobj  
315         LOADL        1
316         CALL         L114
317         LOAD         16[LB]
318         CALLI        L93
319         LOADL        5
320         CALL         L114
321         LOAD         16[LB]
322         CALLI        L93
323         LOADL        8
324         CALL         L114
325         LOAD         16[LB]
326         CALLI        L93
327         LOADL        4
328         CALL         L114
329         LOAD         16[LB]
330         CALLI        L93
331         LOADL        16
332         CALL         L114
333         LOAD         16[LB]
334         CALLI        L93
335         LOADL        11
336         CALL         L114
337         LOAD         16[LB]
338         CALLI        L93
339         LOADL        31
340         CALL         L114
341         LOAD         16[LB]
342         CALLI        L93
343         LOADL        19
344         CALL         L114
345         LOAD         16[LB]
346         CALLI        L93
347         LOADL        13
348         CALL         L114
349         LOAD         16[LB]
350         CALLI        L93
351         LOADL        19
352         CALL         L114
353         LOAD         16[LB]
354         CALLI        L93
355         LOADL        19
356         LOAD         16[LB]
357         CALLI        L105
358         LOADL        11
359         LOAD         16[LB]
360         CALLI        L105
361         LOADL        8
362         LOAD         16[LB]
363         CALLI        L105
364         LOADL        31
365         LOAD         16[LB]
366         CALLI        L98
367         JUMPIF (1)   L37
368         JUMP         L38
369  L37:   LOADL        31
370         CALL         putintnl
371         POP          0
372         JUMP         L39
373  L38:   LOADL        1
374         CALL         neg     
375         CALL         putintnl
376         POP          0
377         JUMP         L39
378  L39:   LOADL        999
379         CALL         putintnl
380         LOADL        5
381         CALL         L59
382         LOAD         17[LB]
383         CALL         L78
384         LOADL        100000
385         CALL         putintnl
386         RETURN (0)   1
387  L40:   LOADL        0
388         JUMP         L44
389  L41:   LOADL        0
390         LOADL        1
391         JUMP         L43
392  L42:   LOADL        2
393         LOAD         4[LB]
394         LOADL        1
395         CALL         add     
396         STORE        4[LB]
397         POP          1
398  L43:   LOAD         4[LB]
399         LOADL        10000
400         CALL         lt      
401         JUMPIF (1)   L42
402         LOAD         3[LB]
403         LOADL        1
404         CALL         add     
405         STORE        3[LB]
406         POP          2
407  L44:   LOAD         3[LB]
408         LOADL        10000
409         CALL         lt      
410         JUMPIF (1)   L41
411         RETURN (0)   0
412  L45:   LOADL        11
413         LOAD         3[LB]
414         CALL         putintnl
415         LOAD         1[OB]
416         LOADL        1
417         LOADA        0[OB]
418         CALL         fieldupd
419         LOADL        12
420         STORE        0[OB]
421         LOAD         1[OB]
422         LOADL        1
423         CALL         fieldref
424         LOADL        0
425         CALL         fieldref
426         STORE        3[LB]
427         LOAD         3[LB]
428         CALL         putintnl
429         LOADL        4
430         STORE        0[OB]
431         LOADL        2
432         LOADL        4
433         LOADL        3
434         LOADA        0[OB]
435         CALLI        L46
436         CALL         add     
437         STORE        3[LB]
438         LOAD         3[LB]
439         CALL         putintnl
440         LOADL        8
441         LOADL        3
442         LOAD         1[OB]
443         CALLI        L50
444         CALL         add     
445         CALL         putintnl
446         LOADL        4
447         STORE        0[OB]
448         LOAD         1[OB]
449         LOADL        0
450         LOADL        5
451         CALL         fieldupd
452         LOADL        2
453         LOADA        0[OB]
454         LOADL        1
455         CALL         fieldref
456         LOADA        0[OB]
457         LOADA        0[OB]
458         CALLI        L47
459         CALL         add     
460         CALL         putintnl
461         RETURN (0)   0
462  L46:   LOAD         0[OB]
463         LOAD         -1[LB]
464         CALL         add     
465         LOAD         -2[LB]
466         CALL         add     
467         RETURN (1)   2
468  L47:   LOAD         -1[LB]
469         LOADL        0
470         CALL         fieldref
471         LOAD         -2[LB]
472         LOADL        0
473         CALL         fieldref
474         CALL         add     
475         LOADA        0[OB]
476         LOADL        0
477         CALL         fieldref
478         CALL         add     
479         RETURN (1)   2
480  L48:   LOADL        7
481         STORE        -1[LB]
482         RETURN (0)   1
483  L49:   LOAD         -1[LB]
484         LOADL        0
485         LOADL        25
486         CALL         fieldupd
487         RETURN (0)   1
488  L50:   LOADL        1
489         LOAD         -1[LB]
490         LOADL        1
491         CALL         gt      
492         JUMPIF (1)   L51
493         JUMP         L52
494  L51:   LOAD         -1[LB]
495         LOAD         -1[LB]
496         LOADL        1
497         CALL         sub     
498         LOADA        0[OB]
499         CALLI        L50
500         CALL         mult    
501         STORE        3[LB]
502  L52:   LOAD         3[LB]
503         RETURN (1)   1
504  L53:   LOADL        1
505         JUMP         L55
506  L54:   LOAD         3[LB]
507         LOAD         -1[LB]
508         CALL         mult    
509         STORE        3[LB]
510         LOAD         -2[LB]
511         LOADL        1
512         CALL         sub     
513         STORE        -2[LB]
514         POP          0
515  L55:   LOAD         -2[LB]
516         LOADL        0
517         CALL         gt      
518         JUMPIF (1)   L54
519         LOAD         3[LB]
520         RETURN (1)   2
521  L56:   LOAD         0[SB]
522         LOADL        3
523         CALL         ne      
524         JUMPIF (1)   L57
525         JUMP         L58
526  L57:   LOADL        3
527         STORE        0[SB]
528  L58:   LOADL        2
529         LOAD         0[SB]
530         CALL         mult    
531         LOADL        2
532         LOAD         -1[LB]
533         CALL         L53
534         CALL         mult    
535         RETURN (1)   1
536  L59:   LOAD         -1[LB]
537         LOADL        2
538         CALL         lt      
539         JUMPIF (1)   L60
540         JUMP         L61
541  L60:   LOADL        0
542         RETURN (1)   1
543         POP          0
544  L61:   LOAD         -1[LB]
545         CALL         newarr  
546         LOAD         3[LB]
547         LOADL        0
548         LOADL        1
549         CALL         arrayupd
550         LOAD         3[LB]
551         LOADL        1
552         LOADL        1
553         CALL         arrayupd
554         LOADL        2
555         JUMP         L63
556  L62:   LOAD         3[LB]
557         LOAD         4[LB]
558         LOAD         3[LB]
559         LOAD         4[LB]
560         LOADL        2
561         CALL         sub     
562         CALL         arrayref
563         LOAD         3[LB]
564         LOAD         4[LB]
565         LOADL        1
566         CALL         sub     
567         CALL         arrayref
568         CALL         add     
569         CALL         arrayupd
570         LOAD         4[LB]
571         LOADL        1
572         CALL         add     
573         STORE        4[LB]
574         POP          0
575  L63:   LOAD         4[LB]
576         LOAD         -1[LB]
577         CALL         lt      
578         JUMPIF (1)   L62
579         LOAD         3[LB]
580         RETURN (1)   1
581  L64:   LOAD         -1[LB]
582         LOAD         -2[LB]
583         CALL         lt      
584         JUMPIF (1)   L65
585         JUMP         L66
586  L65:   LOAD         -2[LB]
587         RETURN (1)   2
588         POP          0
589  L66:   LOAD         -1[LB]
590         RETURN (1)   2
591  L67:   LOAD         -1[LB]
592         LOAD         -1[LB]
593         LOAD         -2[LB]
594         CALL         div     
595         LOAD         -2[LB]
596         CALL         mult    
597         CALL         sub     
598         RETURN (1)   2
599  L68:   LOAD         -1[LB]
600         LOADL        0
601         CALL         eq      
602         JUMPIF (1)   L69
603         JUMP         L70
604  L69:   LOAD         -2[LB]
605         RETURN (1)   2
606  L70:   LOAD         -2[LB]
607         LOADL        0
608         CALL         eq      
609         JUMPIF (1)   L71
610         JUMP         L72
611  L71:   LOAD         -1[LB]
612         RETURN (1)   2
613  L72:   LOAD         -1[LB]
614         LOAD         -2[LB]
615         CALL         eq      
616         JUMPIF (1)   L73
617         JUMP         L74
618  L73:   LOAD         -1[LB]
619         RETURN (1)   2
620  L74:   LOAD         -1[LB]
621         LOAD         -2[LB]
622         CALL         gt      
623         JUMPIF (1)   L75
624         JUMP         L76
625  L75:   LOAD         -2[LB]
626         LOAD         -1[LB]
627         LOAD         -2[LB]
628         CALL         sub     
629         CALL         L68
630         RETURN (1)   2
631  L76:   LOAD         -2[LB]
632         LOAD         -1[LB]
633         CALL         sub     
634         LOAD         -1[LB]
635         CALL         L68
636         RETURN (1)   2
637  L77:   LOAD         -1[LB]
638         LOADL        7
639         CALL         sub     
640         CALL         putintnl
641         RETURN (0)   1
642  L78:   LOADL        0
643         JUMP         L80
644  L79:   LOAD         -1[LB]
645         LOAD         3[LB]
646         CALL         arrayref
647         CALL         putintnl
648         LOAD         3[LB]
649         LOADL        1
650         CALL         add     
651         STORE        3[LB]
652         POP          0
653  L80:   LOAD         3[LB]
654         LOAD         -1[LB]
655         CALL         arraylen
656         CALL         lt      
657         JUMPIF (1)   L79
658         RETURN (0)   1
659         LOAD         -1[LB]
660         LOADL        0
661         CALL         fieldref
662         LOADL        0
663         CALL         eq      
664         JUMPIF (1)   L81
665         JUMP         L82
666  L81:   RETURN (0)   1
667         POP          0
668  L82:   LOAD         -1[LB]
669         LOADL        0
670         CALL         fieldref
671         JUMP         L84
672  L83:   LOAD         3[LB]
673         LOADL        0
674         CALL         fieldref
675         CALL         putintnl
676         LOAD         3[LB]
677         LOADL        1
678         CALL         fieldref
679         STORE        3[LB]
680         POP          0
681  L84:   LOAD         3[LB]
682         LOADL        0
683         CALL         ne      
684         JUMPIF (1)   L83
685         RETURN (0)   1
686  L85:   LOAD         -2[LB]
687         LOADL        1
688         CALL         add     
689         CALL         newarr  
690         LOAD         3[LB]
691         LOADL        0
692         LOADL        0
693         CALL         arrayupd
694         LOADL        1
695         JUMP         L89
696  L86:   LOADL        100
697         CALL         neg     
698         LOADL        0
699         JUMP         L88
700  L87:   LOAD         -1[LB]
701         LOAD         6[LB]
702         CALL         arrayref
703         LOAD         3[LB]
704         LOAD         4[LB]
705         LOAD         6[LB]
706         CALL         sub     
707         LOADL        1
708         CALL         sub     
709         CALL         arrayref
710         CALL         add     
711         LOAD         5[LB]
712         CALL         L64
713         STORE        5[LB]
714         LOAD         6[LB]
715         LOADL        1
716         CALL         add     
717         STORE        6[LB]
718         POP          0
719  L88:   LOAD         6[LB]
720         LOAD         4[LB]
721         CALL         lt      
722         JUMPIF (1)   L87
723         LOAD         3[LB]
724         LOAD         4[LB]
725         LOAD         5[LB]
726         CALL         arrayupd
727         LOAD         4[LB]
728         LOADL        1
729         CALL         add     
730         STORE        4[LB]
731         POP          2
732  L89:   LOAD         4[LB]
733         LOAD         -2[LB]
734         CALL         le      
735         JUMPIF (1)   L86
736         LOAD         3[LB]
737         LOAD         -2[LB]
738         CALL         arrayref
739         RETURN (1)   2
740  L90:   LOAD         -1[LB]
741         STORE        0[OB]
742         RETURN (0)   1
743  L91:   LOADA        0[OB]
744         LOADL        0
745         CALL         fieldref
746         RETURN (1)   0
747         LOADL        15342
748         STORE        0[OB]
749         LOADL        45192
750         STORE        1[OB]
751         LOADL        0
752         STORE        2[OB]
753         RETURN (0)   0
754  L92:   LOAD         2[OB]
755         LOAD         0[OB]
756         CALL         mult    
757         LOAD         1[OB]
758         CALL         add     
759         LOADL        1000
760         LOAD         3[LB]
761         CALL         L67
762         STORE        2[OB]
763         LOAD         2[OB]
764         RETURN (1)   0
765  L93:   LOAD         0[OB]
766         LOADL        0
767         CALL         eq      
768         JUMPIF (1)   L94
769         JUMP         L95
770  L94:   LOAD         -1[LB]
771         STORE        0[OB]
772         RETURN (0)   1
773         POP          0
774  L95:   LOADL        0
775         LOAD         0[OB]
776         JUMP         L97
777  L96:   LOAD         4[LB]
778         STORE        3[LB]
779         LOAD         4[LB]
780         LOADL        1
781         CALL         fieldref
782         STORE        4[LB]
783         POP          0
784  L97:   LOAD         4[LB]
785         LOADL        0
786         CALL         ne      
787         JUMPIF (1)   L96
788         LOAD         3[LB]
789         LOADL        1
790         LOAD         -1[LB]
791         CALL         fieldupd
792         LOAD         1[OB]
793         LOADL        1
794         CALL         add     
795         STORE        1[OB]
796         RETURN (0)   1
797  L98:   LOAD         0[OB]
798         LOADL        0
799         CALL         eq      
800         JUMPIF (1)   L99
801         JUMP         L100
802  L99:   LOADL        0
803         RETURN (1)   1
804         POP          0
805  L100:  LOAD         0[OB]
806         JUMP         L104
807  L101:  LOAD         3[LB]
808         LOADL        0
809         CALL         fieldref
810         LOAD         -1[LB]
811         CALL         eq      
812         JUMPIF (1)   L102
813         JUMP         L103
814  L102:  LOADL        1
815         RETURN (1)   1
816         POP          0
817  L103:  LOAD         3[LB]
818         LOADL        1
819         CALL         fieldref
820         STORE        3[LB]
821         POP          0
822  L104:  LOAD         3[LB]
823         LOADL        0
824         CALL         ne      
825         JUMPIF (1)   L101
826         LOADL        0
827         RETURN (1)   1
828  L105:  LOAD         1[OB]
829         LOADL        0
830         CALL         eq      
831         JUMPIF (1)   L106
832         JUMP         L107
833  L106:  RETURN (0)   1
834         POP          0
835  L107:  LOAD         0[OB]
836         LOADL        0
837         CALL         fieldref
838         LOAD         -1[LB]
839         CALL         eq      
840         JUMPIF (1)   L108
841         JUMP         L109
842  L108:  LOAD         0[OB]
843         LOADL        1
844         CALL         fieldref
845         STORE        0[OB]
846         RETURN (0)   1
847         POP          0
848  L109:  LOAD         0[OB]
849         LOAD         0[OB]
850         LOADL        1
851         CALL         fieldref
852         JUMP         L113
853  L110:  LOAD         4[LB]
854         LOADL        0
855         CALL         fieldref
856         LOAD         -1[LB]
857         CALL         eq      
858         JUMPIF (1)   L111
859         JUMP         L112
860  L111:  LOAD         3[LB]
861         LOADL        1
862         LOAD         4[LB]
863         LOADL        1
864         CALL         fieldref
865         CALL         fieldupd
866         RETURN (0)   1
867         POP          0
868  L112:  LOAD         4[LB]
869         STORE        3[LB]
870         LOAD         4[LB]
871         LOADL        1
872         CALL         fieldref
873         STORE        4[LB]
874         POP          0
875  L113:  LOAD         4[LB]
876         LOADL        0
877         CALL         ne      
878         JUMPIF (1)   L110
879         RETURN (0)   1
880         LOADL        -1
881         LOADL        2
882         CALL         newobj  
883         LOADL        -1
884         LOADL        3
885         CALL         newobj  
886         LOAD         3[LB]
887         LOADL        0
888         LOAD         4[LB]
889         CALLI        L92
890         CALL         fieldupd
891         LOAD         3[LB]
892         RETURN (1)   0
893  L114:  LOADL        -1
894         LOADL        2
895         CALL         newobj  
896         LOAD         3[LB]
897         LOADL        0
898         LOAD         -1[LB]
899         CALL         fieldupd
900         LOAD         3[LB]
901         RETURN (1)   1
