CMPSCI 4280
Fall 2022

Project 2: Code Parser
Takes in a file, and analyzes its contents.  After separating the individual file tokens, it
organizes them into a pre-ordered tree.  The parsing is done according to a given BNF grammar.
Failures found in the tokens or grammar inside the file will cause the process to halt and report the issue.

Invocation:
> frontend [file]

File Parsing Grammar
<program>  ->  <vars> program <block>
<block>    ->  begin <vars> <stats> end
<vars>     ->  empty | whole Identifier :=  Integer  ;  <vars>
<expr>     ->  <N> - <expr>  | <N>
<N>        ->  <A> + <N> | <A> * <N> | <A>
<A>        ->  <M> <B>
<B>        ->  / <M> <B> | empty (NOTE:  This is a revision.  Fixes left recursion originall fount in <A>)
<M>        ->  : <M> |  <R>
<R>        ->  ( <expr> ) | Identifier | Integer
<stats>    ->  <stat>  <mStat>
<mStat>    ->  empty |  <stat>  <mStat>
<stat>     ->  <in> ;  | <out> ;  | <block> | <if> ;  | <loop> ;  | <assign> ; | <goto> ; | <label> ;
<in>       ->  input  Identifier
<out>      ->  output <expr>
<if>       ->  if [ <expr> <RO> <expr> ] then <stat> | if [ <expr> <RO> <expr> ] then <stat> pick <stat>
<loop>     ->  while  [ <expr> <RO> <expr> ]  <stat>
<assign>   ->  assign Identifier  = <expr>
<RO>       ->  >  | < |  ==  |   [ = ]  (three tokens)  | !=
<label>    ->  label Identifier
<goto>     ->  warp Identifier