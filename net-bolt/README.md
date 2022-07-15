tree -L 1

```text
.
├── bolt-broker-server （broker 游戏网关）
├── bolt-client （broker client 游戏逻辑服）
├── bolt-core （游戏网关和逻辑服 ，bolt 相关 core 包）
├── bolt-external （对外服， 也是逻辑服的一种）
└── bolt-run-one （单体启动辅助，一个进程内可以启动 ： 对外服、游戏网关、游戏逻辑服）
```

