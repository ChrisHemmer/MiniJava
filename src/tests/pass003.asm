  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L16
  3         HALT   (0)   
  4  L10:   LOADL        0
  5         JUMP         L12
  6  L11:   LOAD         -1[LB]
  7         LOAD         3[LB]
  8         CALL         arrayref
  9         CALL         putintnl
 10         LOAD         3[LB]
 11         LOADL        1
 12         CALL         add     
 13         STORE        3[LB]
 14         POP          0
 15  L12:   LOAD         3[LB]
 16         LOAD         -1[LB]
 17         CALL         arraylen
 18         CALL         lt      
 19         JUMPIF (1)   L11
 20         RETURN (0)   1
 21  L13:   LOADL        0
 22         JUMP         L15
 23  L14:   LOAD         -1[LB]
 24         LOAD         3[LB]
 25         CALL         arrayref
 26         LOAD         -1[LB]
 27         LOAD         3[LB]
 28         LOAD         -1[LB]
 29         LOAD         -1[LB]
 30         CALL         arraylen
 31         LOADL        1
 32         CALL         sub     
 33         LOAD         3[LB]
 34         CALL         sub     
 35         CALL         arrayref
 36         CALL         arrayupd
 37         LOAD         -1[LB]
 38         LOAD         -1[LB]
 39         CALL         arraylen
 40         LOADL        1
 41         CALL         sub     
 42         LOAD         3[LB]
 43         CALL         sub     
 44         LOAD         4[LB]
 45         CALL         arrayupd
 46         LOAD         3[LB]
 47         LOADL        1
 48         CALL         add     
 49         STORE        3[LB]
 50         POP          1
 51  L15:   LOAD         3[LB]
 52         LOAD         -1[LB]
 53         CALL         arraylen
 54         LOADL        2
 55         CALL         div     
 56         CALL         lt      
 57         JUMPIF (1)   L14
 58         RETURN (0)   1
 59  L16:   LOADL        10
 60         CALL         newarr  
 61         LOADL        0
 62         JUMP         L18
 63  L17:   LOAD         3[LB]
 64         LOAD         4[LB]
 65         LOAD         4[LB]
 66         CALL         arrayupd
 67         LOAD         4[LB]
 68         LOADL        1
 69         CALL         add     
 70         STORE        4[LB]
 71         POP          0
 72  L18:   LOAD         4[LB]
 73         LOAD         3[LB]
 74         CALL         arraylen
 75         CALL         lt      
 76         JUMPIF (1)   L17
 77         LOAD         3[LB]
 78         CALL         L10
 79         LOAD         3[LB]
 80         CALL         L13
 81         LOADL        100
 82         CALL         putintnl
 83         LOAD         3[LB]
 84         CALL         L19
 85         LOADL        100
 86         CALL         putintnl
 87         LOAD         3[LB]
 88         CALL         L10
 89         RETURN (0)   1
 90  L19:   LOADL        2
 91         JUMP         L21
 92  L20:   LOAD         3[LB]
 93         LOADL        1
 94         CALL         add     
 95         STORE        3[LB]
 96         POP          0
 97  L21:   LOAD         -1[LB]
 98         LOAD         3[LB]
 99         CALL         arrayref
100         LOADL        0
101         CALL         ne      
102         JUMPIF (1)   L20
103         LOAD         -1[LB]
104         LOAD         3[LB]
105         CALL         arrayref
106         CALL         putintnl
107         LOAD         -1[LB]
108         LOAD         3[LB]
109         CALL         arrayref
110         CALL         putintnl
111         LOAD         -1[LB]
112         LOAD         3[LB]
113         CALL         arrayref
114         CALL         putintnl
115         RETURN (0)   1
