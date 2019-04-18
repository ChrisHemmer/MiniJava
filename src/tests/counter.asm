  0         PUSH         1
  1         LOADL        0
  2         STORE        0[SB]
  3         PUSH         1
  4         LOADL        0
  5         STORE        1[SB]
  6         LOADL        0
  7         CALL         newarr  
  8         CALL         L12
  9         HALT   (0)   
 10  L10:   LOAD         0[OB]
 11         LOAD         -1[LB]
 12         CALL         add     
 13         STORE        0[OB]
 14         LOAD         1[SB]
 15         LOAD         -1[LB]
 16         CALL         add     
 17         STORE        1[SB]
 18         RETURN (0)   1
 19  L11:   LOAD         0[SB]
 20         LOADL        1
 21         CALL         add     
 22         STORE        0[SB]
 23         RETURN (0)   0
 24  L12:   LOADL        3
 25         CALL         newarr  
 26         LOADL        -1
 27         LOADL        1
 28         CALL         newobj  
 29         LOAD         4[LB]
 30         CALLI        L11
 31         LOADL        -1
 32         LOADL        1
 33         CALL         newobj  
 34         LOAD         5[LB]
 35         CALLI        L11
 36         LOADL        -1
 37         LOADL        1
 38         CALL         newobj  
 39         LOAD         6[LB]
 40         CALLI        L11
 41         LOAD         3[LB]
 42         LOADL        0
 43         LOAD         4[LB]
 44         CALL         arrayupd
 45         LOAD         3[LB]
 46         LOADL        1
 47         LOAD         5[LB]
 48         CALL         arrayupd
 49         LOAD         3[LB]
 50         LOADL        2
 51         LOAD         6[LB]
 52         CALL         arrayupd
 53         LOAD         3[LB]
 54         CALL         L13
 55         LOADL        5
 56         LOAD         4[LB]
 57         CALLI        L10
 58         LOADL        10
 59         LOAD         5[LB]
 60         CALLI        L10
 61         LOADL        15
 62         LOAD         6[LB]
 63         CALLI        L10
 64         LOAD         3[LB]
 65         CALL         L13
 66         LOADL        2
 67         LOAD         4[LB]
 68         CALLI        L10
 69         LOADL        5
 70         CALL         neg     
 71         LOAD         5[LB]
 72         CALLI        L10
 73         LOADL        5
 74         LOADL        3
 75         LOADL        4
 76         CALL         mult    
 77         LOADL        6
 78         CALL         div     
 79         LOAD         1[SB]
 80         CALL         mult    
 81         CALL         add     
 82         LOAD         6[LB]
 83         CALLI        L10
 84         LOAD         3[LB]
 85         CALL         L13
 86         RETURN (0)   1
 87  L13:   LOADL        0
 88         JUMP         L15
 89  L14:   LOAD         -1[LB]
 90         LOAD         3[LB]
 91         CALL         arrayref
 92         LOAD         4[LB]
 93         LOADL        0
 94         CALL         fieldref
 95         CALL         putintnl
 96         LOAD         3[LB]
 97         LOADL        1
 98         CALL         add     
 99         STORE        3[LB]
100         POP          1
101  L15:   LOAD         3[LB]
102         LOAD         -1[LB]
103         CALL         arraylen
104         CALL         lt      
105         JUMPIF (1)   L14
106         LOAD         1[SB]
107         CALL         putintnl
108         RETURN (0)   1
