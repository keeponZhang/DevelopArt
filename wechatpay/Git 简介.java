
Git 简介
git config --local
git config --system


git config --global user.name keeponzhang
git config --global email 462789909@qq.com

git config help
git help config 
man git-config

git config --global --add user.name keepon
git config user.name

git config --get user.name
git config --list --global

git config --global --unset user.name
git config --global --unset user.name zhangwengao

git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.lol "log --oneline"

git 使用40个16进制字符的SHA_1 Hash唯一标识对象
1.blob 
2.tree
3.commit 
4.tag

git init 
git clone 
git init  git_non_bare_repo
git init bare  git_non_bare_repo


git add
git commit 
git status
git rm 
git mv
gitignore

git init git_basics
touch a 
touch b
git add a b
git status 

git commit -m "init commit"
vim a 改变a的内容(test status)
git status
//还没保存到暂存区
git add a 
//添加到暂存区
git commit -m "modify a"


暂存区里面还有个对象库

git rm a （没被追踪的文件要用rm 命令删除）
//把工作区和暂存区的a删除(工作区的文件和暂存区（git的工作区）的索引都删掉了)
git status 
git reset HEAD a
//从history恢复，还原
git rm --cached a (取消暂存)
//暂存区的a删除（暂存区(git工作区)的索引删掉），工作目录不删除

git satus 
git add a

git mv a c
//把a命名为c
git mv c a

mv a c
cmd命令把a命名为c
git status
git add a c

git mv a c
//先cmd命令rename,再 git add
（工作区的文件被重命名了，git的工作区删除了a的索引，新加了c的索引（此时因为对象内容不变，暂存区中的对象库并没有改变你，但是会生成新的tree对象））

git add --A
//把整个工作区添加到暂存区

touch master.txt
master.txt:InitCommit on Master
git add .
git commit -m "InitCommit on Master"

vim master
master.txt:
InitCommit on Master
SecondCommit on Master
git add .
git commit -m "SecondCommit on Master"

git branch test
git checkout test
修改 master.txt （
InitCommit on Master
SecondCommit on Master
Inital commit on test
）
touch test.txt
git add .
git commit -m "Init commit on test"

git checkout master
vim master.txt
(test 分支上的修改没有了)

git log --oneline --decorate --graph -all
git tag "v0"  hash（第一次master提交）
//v0直接指向commit (如果不用hash，默认使用当前指向的commit，也就是现在HEAD 指向的master分支)
git tag "v1" 当前commit

git tag -a "INITAL_COMMIT" hash（第一次master提交）
//会提示输入信息（tag for Inital commit)
是一个Tagger对象，指向了一个commit

git log --oneline --decorate --graph --all



git config --global alias.log "log --oneline --decorate --graph --all"

git show v0
//是一个commit对象
git show INITAL_COMMIT
//是一个tagger对象

git checkout v0
"提示detached HEAD" state
HEAD 引用直接指向了一个commit,而不是分支名，再这样在提交后面直接工作，再切换回其他分支，这部分历史可能丢失掉

可以用下面的命令（用当前的commit创建一个分支，并是切换到这个分支，还是指向v0)
git chekcout -b fix-v0(git branch fix-v0;git chcekout fix-v0)


修改了工作区和暂存区
vim master.txt
修改master.txt:stash1
git add .
git checkout master
(没add的话好像也会提示)
提示please commit your changes or stash them before you switch branches.(因为当前工作区的修改被添加到了暂存区；checkout 的时候会把本地的修改覆盖掉)

git stash save -a "stash 1"
(把暂存区保存起来,此时git lol,会多untracked files on fix-v0: 5a41c98 InitCommit on Master，即使修改的内容没有add到暂存区
)
git status 
（此时很干净的)
git checkout master
git checkout fix_v0
git stash list 
git stash pop --index stash@{0}
{--index 把暂存区也还原回去}
//提示 changes to be committed（stash成功)

vim master.txt(之前添加的stash1也在)

git stash list
//(无内容，git stash pop会清掉)
git stash save -a "stash1"
//(再次把暂存区的改变stash 起来,-a 把暂存区也保存起来)
git stash list
git stash apply --index stash@{0}
({--index 把暂存区也还原回去,不指定哪个stash,默认使用最上面一个)
git commit -m "restore stash1 from stashList"
git stash list
（stash1 还存在)
git stash drop stash@{0}
//(清除掉stash1，不加index,默认使用最上面一个)
git stash list

git stash clear
//(清除全部的stash)


git checkout master
git checkout  -b tst_merge
vim master.txt
(InitCommit on Master
SecondCommit on Master
Initial commit on test_merge)
git add .
git commit -m "Initial commit on test_merge"

git lol

git checkout master
git merge test_merge
//提示是 fast-forward,表示test_merge都是在master分支的当前指向的commit上产生的衍生提交，不会再产生一个commit，只需要把工作区和暂存区恢复到test_merge一样的状态，然后把master指向test_merge指向的那个commit（Initial commit on test_merge）

git lol
git merge test
//(test跟test_merge都是master提交两次后拉出来的)
此时提示Auto-merging master.txt
CONFLICT (content): Merge conflict in master.txt

git merge --abort
//放弃合并

手动解决冲突
git add master.txt
git commit
提示 merge branch test 
conflicts master.txt
:wq退出
 此时提交信息为Merge branch 'test'

分支切换是HEAD引用的移动以及暂存区和工作区的还原。

git log --oneline --decorate --graph --all
git show de82fa8
git show master
git show HEAD

git show master^
git show master~
第一父提交
git show master^2
第二父提交

git show --oneline master^2
git show --stat master^2
git show --oneline --stat master^2

git show v0
git show --format=%T master^2

git show (第一行的hash值)
tree 67f180

git show(index 后面的第一个键值对的键)
InitCommit on Master
SecondCommit on Master

git show(index 后面的第一个键值对的值)
InitCommit on Master
SecondCommit on Master
Inital commit on test

git log 
git log -p
git log --stat 
//差异的统计信息
git log --oneline

git diff
//工作区与暂存区的差异
git diff HEAD
//工作区与历史提交的差异
git diff --cache
暂存区与历史提交的差异

git diff HEAD HEAD~2
//两个commit的差异
git diff HEAD HEAD~2 -- master.txt
//两个commit中指定文件的差异

git diff --color-words
git diff --word-diff



vim master.txt
//添加：This will be discard by staging area.
git status

git checkout -- master.txt
//撤销工作区的修改，其实是使用暂存区的覆盖工作区

vim master.txt
//添加：This will be discard by staging area.
git add master.txt
git status

git reset master.txt
(等同于 git reset HEAD master.txt，等同于git reset --mixed HEAD master.txt，重置提交记录为HEAD，用HEAD历史记录恢复到暂存区，工作区不变；git reset --soft HEAD master.txt,重置提交记录为HEAD,暂存区，工作区不变，此时无变化；git reset --hard HEAD master.txt，重置提交记录为HEAD，把HEAD的内容恢复到暂存区，工作区)
git diff --cached
(比较暂存区与提交记录，此时为差异)
git status
提示not staged for commit (因为master修改后虽然添加到了暂存区，但是用git reset master.txt 表示用HEAD历史记录恢复到暂存区，等同于master没有被添加到暂存区)

git checkout 和git reset:
1.git checkout [branch] 与运行 git reset --hard [branch] 非常相似,都会更新所有的三棵树
两点区别:
(1)首先不同于 reset --hard，checkout 对工作目录是安全的，它会通过检查来确保不会将已更改的文件弄丢；而 reset --hard 则会不做检查就全面地替换所有东西。
(2).第二个重要的区别是如何更新 HEAD。 reset 会移动 HEAD 分支的指向（即 HEAD 指向的分支的指向），而 checkout 只会移动 HEAD 自身来指向另一个分支。 
https://blog.csdn.net/longintchar/article/details/82314102

不带file:
git reset [commit]和git checkout 都会移动HEAD
区别：set 会移动 HEAD 分支的指向，而 checkout 则移动 HEAD 自身.例如当前为develop分支，调用git reset master，
develop的HEAD节点会移到master指向的最后一次commit中。



带file:(reset的commit省的话用HEAD,checkout的commit不能省)
git reset (commit) [file] 和git checkout (commit) [file] 都不会移动HEAD
 区别:git reset (commit) [file] 等同于git reset --mixted(commit) [file],有使用指定commit的file覆盖暂存区的效果，而
git checkout (commit) [file]等同于git reset --hard(commit) [file] ，不仅用某次提交中的那个文件来更新索引，同时也会覆盖工作目录中对应的文件 —— 这样对工作目录并不安全！
注意：一旦暂存了，想恢复到工作区，只能用 git reset HEAD <file>(此时使用上一次提交的历史记录还原暂存区，工作区不变，因为暂存区是上次提交的状态，所以此时工作区的状态是，有修改，未暂存)，不想恢复到工作区，直接还原到上次暂存区状态，用git checkout HEAD <file>(等同git reset --hard(commit) [file] )；没暂存，可以用git checkout -- <file>清除工作区的修改
https://blog.csdn.net/longintchar/article/details/82314102

git show INITIAL_COMMIT
git checkout INITIAL_COMMIT -- master.txt
git status
//提示 change to be committed ，表示工作区与暂存区是一样的
git diff --cached
查看暂存区与历史提交的不同

git checkout HEAD --master.txt
(可以抵消上面的git checkout INITIAL_COMMIT -- master.txt命令)




touch .gitnore
git add .gitignore
touch test.o test.c
//把 test.c加入gitignore
git clean -n
//清理gitignore以外的文件(未被跟踪且没被忽略的文件)
 git clean -n -X
//清理gitignore以内的文件
git clean -f 
//强制清除

//在test.o中新加一行 first line.
git add .
git commit -m "SecondCommit"
git revert HEAD
//可以修改提交信息获取按WQ退出，发现first line被删除掉了。


git revert 
//做某次提交的相反操作


git commit --amend
//产生一个新的提交，替换当前的提交
//前提是当前暂存区的内容已经获取到当前提交的上一次提交状态，并且还要做出改变。
git rebase
//可以产生一个线性提交
git reset
git reflog
//维护了一个HEAD的历史信息，通常配合git reset一起使用

//如果想撤销revert，由不想有一个新的提交记录，可以用git commit --amend
//因为git revert 改变的是test.o文件，删除了第一行，首先我们使用git checkout HEAD~ -- 还原具体的文件
git checkout HEAD~ -- test.o
//此时工作区和暂存区就有了test.o文件
//接着在test.o文件下一行添加一行文字，test amend.
git add test.o
//重新加入暂存区
git commit --amend
//修改提交信息,改为test amend. 现在只有三个提交记录，firstcommit,secondcommit,test amend.


git rebase
//首先在test.o文件中加一行 This is a confilct rebase;
git add .
git commit -m "this is conflict rebase commit"


git checkout -b test_rebase HEAD~
//检出之前的一个提交作为分支test_rebase(此时test.o文件只有两行文字)
//接着修改test.o文件，this is first commmit on test_rebase.
git add .
git commit -m "this is firstcommit on test_rebase"


git rebase master
//会提示冲突，可以使用 git rebase --abort 放弃
git rebase --abort
//放弃rebase
git rebase master
//重新发起rebase ,手动修改冲突，改完之后，不是用commit命令，而是用git rebase --continue
git rebase --continue
//提示test.o: needs merge
// You must edit all merge conflicts and then
// mark them as resolved using git add
git add test.o
//将test.o加入到暂存区
git rebase --continue
//提示Applying: this is firstcommit on test_rebase












