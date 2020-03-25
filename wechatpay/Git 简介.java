
Git 简介
vim命令:　
:w 保存当前编辑文件，但并不退出
:q 退出不保存
:wq 保存退出

// 当前仓库
git config --local
//当前登录用户，c/users/当前用户名/.gitconfig
git config --global
//全局 C:\Program Files\Git\mingw64\etc gitconfig
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


当执行 git status 的时候，返回结果大致可分为3个部分：

拟提交的变更：这是已经放入暂存区，准备使用 git commit 命令提交的变更
未暂存的变更：这是工作目录和暂存区快照之间存在差异的文件列表
未跟踪的文件：这类文件对于 Git 系统来说是未知的，也是可以被忽略的
如果在 git status 命令后面加上 --ignored选项，还会列出被忽略的文件。 

还有一种简洁的输出格式，即添加 --short 选项，例如

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
git rm --cached a (取消暂存,add 的相反操作（已经commit 了也可用）)
//暂存区的a删除（暂存区(git工作区)的索引删掉），工作目录不删除

git satus 
git add a

git mv a c
//把a命名为c
//先cmd命令rename,再 git add
//（工作区的文件被重命名了，git的工作区删除了a的索引，新加了c的索引（此时因为对象内容不变，暂存区中的对象库并没有改变你，但是会生成新的tree对象））

mv a c
cmd命令把a命名为c
git status
git add a c



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
(没add的话好像也会提示)(两者有冲突的话，新加的话好像不会)
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
{--index 把暂存区也还原回去(因为已经暂存的也可以stash)}
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
//提示是 fast-forward,表示test_merge都是在master分支的当前指向的commit上产生的衍生提交，不会再产生一个commit，
//只需要把工作区和暂存区恢复到test_merge一样的状态，然后把master指向test_merge指向的那个commit（Initial commit on test_merge）

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
//删除分支
 git branch -d
 git branch -D
git log 
git log -p
git log --stat 
//差异的统计信息
git log --oneline

git diff
//工作区与暂存区的差异 (一个文件修改后，如果没有暂存，git diff会提示不同；如果暂存了，此时工作区与暂存区一致，git diff不会提示)
git diff HEAD
//工作区与历史提交的差异(一个文件修改后，如果没有暂存，git diff会提示不同；如果暂存了，此时工作区与暂存区一致，因为是比较工作区与历史提交，所以git diff也会提示)
git diff --cached
//暂存区与历史提交的差异 (一个文件修改后，如果没有暂存，git diff不会提示不同；如果暂存了，暂存区有东西，因为是比较工作区与历史提交，所以git diff会提示)

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

一：文件从暂存区回退到工作区，撤销add

　　如果想取消某个add的文件，可以使用该命令来进行撤销操作

　　撤消add：git reset 文件名

　　撤消所有add的文件：git reset HEAD .

　　撤消某个文件或文件夹：git reset HEAD 文件（夹）名

　　把从cache中删除的文件，重新添加到cache中： git add -f 文件名

    eg：今天add文件的时候，不小心使用了git add .  将配置文件等也给add到缓存区了，那么如何从缓存区撤销我们不想add的文件呢？ 使用命令： git  reset  fileName

二：版本回退 

1. git reset  --mixed commit的id  

还原到commit的id，（git reset 默认是mixed ）此commit之后的文件变成modified红色（即还没add的状态）

2. git reset  --soft commit的id 

还原到commit的id，此commit之后的文件变成modified绿色（即add（勾选中）的状态）

3. git reset --hard commit的id 

还原到commit的id，此commit之后的文件都被还原


git reset (–mixed) HEAD~1 
回退一个版本,且会将暂存区的内容和本地已提交的内容全部恢复到未暂存的状态,不影响原来本地文件(未提交的也 
不受影响) 
git reset –soft HEAD~1 
回退一个版本,不清空暂存区,将已提交的内容恢复到暂存区,不影响原来本地的文件(未提交的也不受影响) 
git reset –hard HEAD~1 
回退一个版本,清空暂存区,将已提交的内容的版本恢复到本地,本地的文件也将被恢复的版本替换



参考三棵树：
https://blog.csdn.net/longintchar/article/details/82314102
git reset master.txt
(等同于 git reset HEAD master.txt，等同于git reset --mixed HEAD master.txt）

git reset --soft HEAD （不能带文件名）重置提交记录为，用HEAD无效果，因为带 --soft只移动 HEAD 指针，工作区和暂存区的内容不变；git reset --soft HEAD~ 相当于把HEAD指针移动上次提交，此时可以再次提交或者更改描述再提交）
git reset --mixed HEAD ，重置提交记录为HEAD，（–mixed还会用HEAD 指向的当前快照的内容来更新索引（暂存区与当前历史提交一致），工作区内容不变。
git reset --hard HEAD ，（不能带文件名）重置提交记录为HEAD，（–mixed还会用HEAD 指向的当前快照的内容来更新索引（暂存区)和工作区）(谨慎使用，工作区的内容会丢掉)。

(git reset  HEAD master.txt 带文件名不会一定HEAD指针)
--soft 表示当前HEAD提交与暂存区和工作区不一定一致（所以重置HEAD指针之前的修改都还会保留在暂存区和工作区）
--mixed 表示当前HEAD提交与暂存区一致（所以重置HEAD指针之前的修改都还会保留在工作区）
--hard 表示当前HEAD提交与暂存区和工作区一致（所以重置HEAD指针之前的修改都没了）

git reset --soft HEAD~2可以实现压缩提交，把两次提交压缩成一次


git diff --cached
(比较暂存区与提交记录，此时为差异)
git status
提示not staged for commit (因为master修改后虽然添加到了暂存区，但是用git reset master.txt 表示用HEAD历史记录恢复到暂存区，等同于master没有被添加到暂存区)

git checkout 和git reset:
1.git checkout [branch] 与运行 git reset --hard [branch] 非常相似,都会更新所有的三棵树
两点区别:
(1)首先不同于 reset --hard，checkout 对工作目录是安全的，它会通过检查来确保不会将已更改的文件弄丢；而 reset --hard 则会不做检查就全面地替换所有东西。
(2).第二个重要的区别是如何更新 HEAD。 reset 会移动 HEAD 分支的指向（即 HEAD 指向的分支的指向），而 checkout 只会移动 HEAD 自身来指向另一个分支。 


不带file:
git reset [commit]和git checkout 都会移动HEAD
区别：set 会移动 HEAD 分支的指向，而 checkout 则移动 HEAD 自身.例如当前为develop分支，调用git reset master，
develop的HEAD节点会移到master指向的最后一次commit中。



带file:(reset，的commit省的话用HEAD,checkout省的话表示用当前的暂存区)
git reset (commit) [file] 和git checkout (commit) [file] 都不会移动HEAD
 区别:git reset (commit) [file] 等同于git reset --mixted(commit) [file],有使用指定commit的file覆盖暂存区的效果，而
git checkout (commit) [file]等同于git reset --hard(commit) [file] ，不仅用某次提交中的那个文件来更新索引，同时也会覆盖工作目录中对应的文件 —— 这样对工作目录并不安全！
注意：一旦暂存了，想恢复到工作区，只能用 git reset HEAD <file>(此时使用上一次提交的历史记录还原暂存区，工作区不变，因为暂存区是上次提交的状态，所以此时工作区的状态是，有修改，未暂存)，
不想恢复到工作区，直接还原到上次暂存区状态，用git checkout HEAD <file>(等同git reset --hard(commit) [file] )；没暂存，可以用git checkout -- <file>清除工作区的修改
https://blog.csdn.net/longintchar/article/details/82314102



git checkout 命令详解
git checkout [<commit>] [--] <paths>  用于拿暂存区的文件覆盖工作区的文件，或者用指定提交中的文件覆盖暂存区和工作区中对应的文件。
注意: 如果path还没暂存,git checkout test.txt 等于git checkout HEAD test.txt；如果path暂存了，git checkout test.txt 不等于等于git checkout HEAD test.txt，git checkout test.txt 默认拿暂存区覆盖工作区
git checkout <branch> 用于切换分支。
git checkout -b <new_branch> [<start_point>] 用于创建并切换分支


工作目录下面的所有文件都不外乎这两种状态：已知（已跟踪）的和未知的。已跟踪的文件是指已经被纳入版本控制的文件，
未知的文件又分为两种：未跟踪和和已忽略（这个以后再说）。
已跟踪的文件可分为以下几种状态：已提交（或未修改）；已修改；已暂存





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


//拣选会提取某次提交的补丁，之后尝试将其重新应用到当前分支上
git cherry-pick e43a6

同 merge 操作一样，拣选操作也可能产生冲突。有人会问：不会吧，打个补丁也能冲突？当然能。
用 diff 工具生成 patch 时，我们所做的每一处修改都会连同它的“定位信息”（原始文件中的行号、修改处前三行和后三行的原始文本）一并保存到 patch 文件中。patch 被应用时，会在目标文件中寻找“定位信息”，找到后再实施修改。可是，当我们把补丁应用到 C7 上时，有可能找不到那些定位信息了：在master分支上，C2变成了C3，在maint分支上，C2变成了C6，又变成了C7，也许C3和C7相差越来越远，C3中的上下文在C7中早已面目全非，不见踪迹。于是应用patch失败，即发生冲突。

git rebase
其实“git rebase”就是一系列的“cherry-pick”，只是这一系列的动作用一条命令（git rebase）给完成了。你完全可以通过多次手动“cherry-pick”来复制其行为（不过不太方便，更容易出现人为错误）
在 rebase 的过程中，也许会出现冲突 (conflict)。如果遇到冲突，Git 会停止 rebase，并让你去解决冲突；在解决完冲突后，可以用"git-add"命令去标记此冲突已经解决。 然后，你无需执行 git commit,只要执行:
git rebase --continue
这样 git 会继续应用余下的补丁。
如果你rebase到一半，突然后悔了，你可以用--abort参数来终止 rebase，并且"mywork" 分支会回到 rebase 开始前的状态。
git rebase --abort




