/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.action.skeleton.toy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class BannerData {

    List<String> listData() {
        List<String> bannerList = new ArrayList<>();

        String banner;

        banner = """
                d8b           .d8888b.                                 
                Y8P          d88P  Y88b                                
                             888    888                                
                888  .d88b.  888         8888b.  88888b.d88b.   .d88b. 
                888 d88""88b 888  88888     "88b 888 "888 "88b d8P  Y8b
                888 888  888 888    888 .d888888 888  888  888 88888888
                888 Y88..88P Y88b  d88P 888  888 888  888  888 Y8b.    
                888  "Y88P"   "Y8888P88 "Y888888 888  888  888  "Y8888 
                """;

        bannerList.add(banner);

        banner = """
                  ,,
                  db             .g8""\"bgd
                               .dP'     `M
                `7MM  ,pW"Wq.  dM'       `  ,6"Yb.  `7MMpMMMb.pMMMb.  .gP"Ya
                  MM 6W'   `Wb MM          8)   MM    MM    MM    MM ,M'   Yb
                  MM 8M     M8 MM.    `7MMF',pm9MM    MM    MM    MM 8M""\"""\"
                  MM YA.   ,A9 `Mb.     MM 8M   MM    MM    MM    MM YM.    ,
                .JMML.`Ybmd9'    `"bmmmdPY `Moo9^Yo..JMML  JMML  JMML.`Mbmmd'
                """;


        bannerList.add(banner);

        banner = """
                ██╗ ██████╗  ██████╗  █████╗ ███╗   ███╗███████╗
                ██║██╔═══██╗██╔════╝ ██╔══██╗████╗ ████║██╔════╝
                ██║██║   ██║██║  ███╗███████║██╔████╔██║█████╗
                ██║██║   ██║██║   ██║██╔══██║██║╚██╔╝██║██╔══╝
                ██║╚██████╔╝╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗
                ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝
                """;

        bannerList.add(banner);

        banner = """
                 /$$            /$$$$$$                                  \s
                |__/           /$$__  $$                                 \s
                 /$$  /$$$$$$ | $$  \\__/  /$$$$$$  /$$$$$$/$$$$   /$$$$$$\s
                | $$ /$$__  $$| $$ /$$$$ |____  $$| $$_  $$_  $$ /$$__  $$
                | $$| $$  \\ $$| $$|_  $$  /$$$$$$$| $$ \\ $$ \\ $$| $$$$$$$$
                | $$| $$  | $$| $$  \\ $$ /$$__  $$| $$ | $$ | $$| $$_____/
                | $$|  $$$$$$/|  $$$$$$/|  $$$$$$$| $$ | $$ | $$|  $$$$$$$
                |__/ \\______/  \\______/  \\_______/|__/ |__/ |__/ \\_______/
                """;
        bannerList.add(banner);

        banner = """
                .------..------..------..------..------..------.
                |I.--. ||O.--. ||G.--. ||A.--. ||M.--. ||E.--. |
                | (\\/) || :/\\: || :/\\: || (\\/) || (\\/) || (\\/) |
                | :\\/: || :\\/: || :\\/: || :\\/: || :\\/: || :\\/: |
                | '--'I|| '--'O|| '--'G|| '--'A|| '--'M|| '--'E|
                `------'`------'`------'`------'`------'`------'
                """;

        bannerList.add(banner);

        return bannerList;

    }
}
