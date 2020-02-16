package com.example.mygame.Bot;

import android.widget.ImageButton;

import androidx.core.util.Pair;

import com.example.mygame.TicTacToe.Game.Game;

import java.util.Vector;

public class BotCircle implements Bot{
    private Game game;
    private ImageButton[][] buttons;
    private int NUMBERS_OF_SYM_TO_WIN = 0;
    private int DIFFICULTY = 1; //Сложность бота. 1 - самое простое, 2 - средняя, 3 - максимальная

    public BotCircle (ImageButton[][] buttons, int numberOfSymToWin, int difficulty){
        this.buttons = buttons;
        this.NUMBERS_OF_SYM_TO_WIN = numberOfSymToWin;
        this.DIFFICULTY = difficulty;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void setButtons(ImageButton[][] buttons){
        this.buttons = buttons;
    }

    @Override
    public boolean makeTurnAsBot() {
        double rand = Math.random();
        double hardnessCoefficient = 1;
        switch (DIFFICULTY){
            case 1: hardnessCoefficient = 0.1;
            break;
            case 2: hardnessCoefficient = 0.6;
            break;
            case 3: hardnessCoefficient = 5;
        }
        //Чем ниже сложность, тем меньше шансов, что бот будет "думать" и выше - что выберет случайную
        //ячейку
        try {
            if (rand <= hardnessCoefficient) {

                //Проверить на победные ходы (Бот играет за О)
                Vector<Pair<Integer, Integer>> buf;
                for (int i = 1; i < NUMBERS_OF_SYM_TO_WIN; i++) {
                    buf = checkFieldToPrevictoryOrPredefeat("O", "X", NUMBERS_OF_SYM_TO_WIN - i, i);
                    if (buf.size() > 0) {


                        int numberOfButtons = 0; //Количество доступных для выбора ячеек
                        int randomChoseAnotherCount = 1; // номер ячейки из выборки всех доступных ячеек.
                        double randomNumber = Math.random();

                        for (Pair<Integer, Integer> onePair : buf)
                            if (game.getField()[onePair.first][onePair.second].getPlayer().getSymbol().equals("")) {
                                numberOfButtons++;
                            }


                        for (Pair<Integer, Integer> onePair : buf) {
                            if (game.getField()[onePair.first][onePair.second].getPlayer().getSymbol().equals("")) {
                            /*Какой шанс, что мы выберем другую кнопку.
                             Если у нас на выбор три кнопки, то первая может быть выбрана с шансом
                             1/3. Если число больше, то мы выбираем оставшиеся две с шансом 1/2.
                             Если */
                                if (randomNumber <= (randomChoseAnotherCount / (double) numberOfButtons)) {
                                    if (game.getField()[onePair.first][onePair.second].isFilled())
                                        return false;
                                    click(onePair.first, onePair.second);
                                    return true;
                                }
                            }
                            randomChoseAnotherCount++;
                        }
                    }
                    if (i <= 2) {
                        buf = checkFieldToPrevictoryOrPredefeat("X", "O", NUMBERS_OF_SYM_TO_WIN - i, i);
                        if (buf.size() > 0) {
                            /*Тут есть тонкость. Если противник поставит знаки _ X _ X _, то это считается победной комбинацией
                             * Но куда поставить символ бот не будет знать. Поэтому надо проверить все ячейки
                             * до предпредпоследней, на соответсвие паттерну, X _ X, чтобы предотвратить это.*/
                            Pair<Integer, Integer> onePairForB = new Pair<>(-1, -1);
                            for (int k = 0; k < buf.size() - 2; k++) {
                                if (game.getField()[buf.elementAt(k).first][buf.elementAt(k).second].getPlayer().getSymbol().equals("X")
                                        && game.getField()[buf.elementAt(k + 1).first][buf.elementAt(k + 1).second].getPlayer().getSymbol().equals("")
                                        && game.getField()[buf.elementAt(k + 2).first][buf.elementAt(k + 2).second].getPlayer().getSymbol().equals("X")) {
                                    onePairForB = new Pair<>(buf.elementAt(k + 1).first, buf.elementAt(k + 1).second);
                                }
                            }
                            /*Иначе просто ставим на любую клетку*/
                            if (onePairForB.first == -1) {
                                for (Pair<Integer, Integer> onePair : buf) {
                                    if (game.getField()[onePair.first][onePair.second].getPlayer().getSymbol().equals("")) {
                                        onePairForB = onePair;
                                    }
                                }
                            }
                            if (onePairForB.first != -1) {
                                if (game.getField()[onePairForB.first][onePairForB.second].isFilled()) {
                                    return false;
                                }
                                click(onePairForB.first, onePairForB.second);
                                return true;
                            }
                        }
                    }
                }
                //Если мы в начале игры или все доступные позиции для развития атаки заняты
                //Выбираем случайную из центральных ячеек если размер поля четный
                //И центральную, если нечетный
                double randomCenter = game.getField().length / 2.0;
                if (game.getField().length % 2 == 0) {
                    double random = Math.random();
                    randomCenter =
                            (random < 0.5 ? randomCenter : randomCenter + 1);
                } else {
                    randomCenter = Math.floor(randomCenter);
                }
                if (game.getField()[(int) randomCenter][(int) randomCenter].getPlayer().getSymbol().equals("")) {
                    click((int) randomCenter, (int) randomCenter);
                    return true;
                }
                double random = Math.random();
                Pair<Integer, Integer> randomAngle =
                        (random < 0.25 ? new Pair<Integer, Integer>(0, 0) :
                                (random < 0.5 ? new Pair<Integer, Integer>(0, game.getField()[0].length - 1) :
                                        random < 0.75 ? new Pair<Integer, Integer>(game.getField().length - 1, 0) :
                                                new Pair<Integer, Integer>(game.getField().length - 1, game.getField()[2].length - 1)));
                if (game.getField()[randomAngle.first][randomAngle.second].getPlayer().getSymbol().equals("")) {
                    click(randomAngle.first, randomAngle.second);
                    return true;
                }
            }
            /*Если у бота нет идей, то просто случайно заполняем клетки*/
            Vector<Pair<Integer, Integer>> allUnfilledSquares = new Vector<>();
            for (int i = 0; i < game.getField().length; i++) {
                for (int j = 0; j < game.getField()[i].length; j++) {
                    if (game.getField()[i][j].getPlayer().getSymbol().equals(""))
                        allUnfilledSquares.add(new Pair<Integer, Integer>(i, j));
                }
            }
            double randomNumber = Math.random();
            for (int i = 0; i < allUnfilledSquares.size(); i++) {
                if (randomNumber <= (i + 1 / (double) allUnfilledSquares.size())) {
                    click(allUnfilledSquares.elementAt(i).first, allUnfilledSquares.elementAt(i).second);
                    return true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex){
            Vector<Pair<Integer, Integer>> allUnfilledSquares = new Vector<>();
            for (int i = 0; i < game.getField().length; i++) {
                for (int j = 0; j < game.getField()[i].length; j++) {
                    if (game.getField()[i][j].getPlayer().getSymbol().equals("")) {
                        allUnfilledSquares.add(new Pair<Integer, Integer>(i, j));
                    }
                }
            }
            double randomNumber = Math.random();
            for (int i = 0; i < allUnfilledSquares.size(); i++) {
                if (randomNumber <= (i + 1 / (double) allUnfilledSquares.size())) {
                    click(allUnfilledSquares.elementAt(i).first, allUnfilledSquares.elementAt(i).second);
                    return true;
                }
            }
        }
        return false;
    }




    private Vector<Pair<Integer, Integer>> checkFieldToPrevictoryOrPredefeat
            (final String requiredSymbol, final String enemySymbol, final int minElInARow, final int minNullSquaresInARow)
    {
        for(int i = 0; i < game.getField().length; i++){
            for(int j = 0; j < game.getField()[i].length; j++) {
                //Если нашли нужный символ проверяем сначала столбец
                if (game.getField()[i][j].getPlayer().getSymbol().equals(requiredSymbol)) {
                        /*
                        Мы идем сначала вверх по столбцу и проверяем, какие клетки есть сверху
                        от проверяемого элемента. А после идем вниз по столбцу. Результирующим
                        значением будет количество символов и пустых клеток ДО вражеского символа.
                        Все, что идет после вражеского символа мы не рассматриваем, так как комбинацию
                        после него не построить.
                        * */

                    Vector<Pair<Integer, Integer>> vertVec = new Vector<>();
                    /*Проверка по вертикали*/
                    int lineWeight = 10;
                    for (int k = i - 1; k >= 0; k--) {
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    for (int k = i + 1; k < game.getField().length; k++) {
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[k][j].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    /*
                    * Если в строке/столбце/диагонали есть как минимум свободных клеток
                    * (если своих клеток уже, скажем, 2, то минимум одна клетка, иначе
                    * И
                    * В столбце/строке/диагонали есть минимально требуемое количество требуемых
                    * клеток (так, для препобедного нужно минимум 2, иначе хотя бы 1)
                    * */
                    if ((lineWeight % 10 >= minNullSquaresInARow) && ((lineWeight / 10) >= minElInARow)) {
                        Vector<Pair<Integer, Integer>> retVector = new Vector<>();
                        for (int l = 0; l < game.getField().length; l++)
                            retVector.add(new Pair<Integer, Integer>(l, j));
                        vertVec = retVector;//Заменено на рандомную версию
                    }

                    lineWeight = 10;



                    Vector<Pair<Integer, Integer>> horVec = new Vector<>();
                    //Проверка по горизонтали


                    for (int k = j - 1; k >= 0; k--) {
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    for (int k = j + 1; k < game.getField()[i].length; k++) {
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[i][k].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    /*Если есть пустые клетки в строке и больше одной занятой клетки
                     * или пустых клеток в сумме больше или равно, чем необходимых клеток для
                     * победы */
                    if ((lineWeight % 10 >= minNullSquaresInARow) && ((lineWeight / 10) >= minElInARow)) {
                        Vector<Pair<Integer, Integer>> retVector = new Vector<>();
                        for (int l = 0; l < game.getField()[i].length; l++)
                            retVector.add(new Pair<Integer, Integer>(i, l));
                        horVec = retVector;
                    }



                    lineWeight = 10;
                    Vector<Pair<Integer, Integer>> mainDiagVec = new Vector<>();
                    //Проверка по диагонали(главной)

                    for (int x = j - 1, y = i - 1; x >= 0 && y>=0; x--, y--) {
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    for (int x = j + 1, y = i + 1; x < game.getField()[i].length && y < game.getField().length; x++, y++) {
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    /*Если есть пустые клетки в строке и больше одной занятой клетки
                     * или пустых клеток в сумме больше или равно, чем необходимых клеток для
                     * победы */
                    if ((lineWeight % 10 >= minNullSquaresInARow) && ((lineWeight / 10) >= minElInARow)) {
                        Vector<Pair<Integer, Integer>> retVector = new Vector<>();
                        for (int x = 0, y = 0; x < game.getField()[i].length || y < game.getField().length; x++, y++)
                            retVector.add(new Pair<Integer, Integer>(y, x));
                        mainDiagVec = retVector;
                    }

                    lineWeight = 10;
                    Vector<Pair<Integer, Integer>> notMainDiagVec = new Vector<>();


                    //Проверка по диагонали подчиненной

                    for (int x = j + 1, y = i - 1; x < game.getField()[i].length && y>=0; x++, y--) {
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    for (int x = j - 1, y = i + 1; x >=0 && y < game.getField().length; x--, y++) {
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(requiredSymbol))
                            lineWeight += 10;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(""))
                            lineWeight += 1;
                        if (game.getField()[y][x].getPlayer().getSymbol().equals(enemySymbol))
                            break;
                    }
                    /*Если есть пустые клетки в строке и больше одной занятой клетки
                     * или пустых клеток в сумме больше или равно, чем необходимых клеток для
                     * победы */
                    if ((lineWeight % 10 >= minNullSquaresInARow) && ((lineWeight / 10) >= minElInARow)) {
                        Vector<Pair<Integer, Integer>> retVector = new Vector<>();
                        for (int x = 0, y = game.getField()[i].length - 1; x < game.getField()[i].length || y >= 0; x++, y--)
                            retVector.add(new Pair<Integer, Integer>(y, x));
                        notMainDiagVec = retVector;
                    }

                    //Собираем все ненулевые результаты
                    Vector<Vector<Pair<Integer, Integer>>> results = new Vector<>();
                    if(vertVec.size() > 0) results.add(vertVec);
                    if(horVec.size() > 0) results.add(horVec);
                    if(mainDiagVec.size() > 0) results.add(mainDiagVec);
                    if(notMainDiagVec.size() > 0) results.add(notMainDiagVec);
                    double random = Math.random();
                    for (int k = 0; k < results.size(); k++) {
                        if (random <= ((k + 1) / (double) results.size())) {
                            return results.elementAt(k);
                        }
                    }
                }
            }

        }
        return new Vector<>();
    }

    private void click(int x, int y){
        //game.getField()[x][y].fill(game.getCurrentActivePlayer());
        buttons[x][y].callOnClick();
        game.setFilledSquaresCount(game.getFilledSquaresCount() + 1);
    }

    /*private Vector<Pair<Integer, Integer>> foundSymbolToMakeMore(){
        Vector<Pair<Integer, Integer>> result = new Pair[0];
        for(int i = 0; i < game.getField().length; i++){
            for(int j = 0; j < game.getField()[i].length; j++){
                if(game.getField()[i][j].getPlayer()!=null) {
                    if (game.getField()[i][j].getPlayer().getSymbol().equals("O")){
                        //Проверка, есть ли на линии с О крестик
                        //Если нет, то на линии либо два пустых места, либо два О и один Х, либо
                        // два Х и один О, причем в обоих случаях от строки/столбца нет смысла
                        //Первая проверка - на столбец, вторая - на строку, третья - на диагональ
                        Vector<Pair[]> results = new Vector<>();
                        Pair[] vert = checkVertical(i, j);
                        if (vert.length > 0) results.add(vert);
                        Pair[] hori = checkHorizontal(i, j);
                        if (hori.length > 0) results.add(hori);
                        Pair[] diag = checkDiagonal(i, j);
                        if (diag.length > 0) results.add(diag);
                        double random = Math.random();
                        for(int k = 0; k < results.size(); k++){
                            if(random <= ((k+1)/(double)results.size())){
                                result = results.elementAt(k);
                                break;
                            }
                        }
                        //*----------------------

                        //--------------------------
                        //Если мы находимся на главной диагонали

                    }
                }
            }
        }
        return  result;
    }

    private Vector<Pair<Integer, Integer>> checkVertical(int i, int j){
        int linePrice = 0;
        for(int k = i-1; k >= 0; k--){
            if(game.getField()[k][j].getPlayer().getSymbol().equals("")) {
                linePrice++;
            } else {
                break;
            }
        }
        for(int k = i+1; k <= game.getField().length-1; k++){
            if(game.getField()[k][j].getPlayer().getSymbol().equals("")) {
                linePrice++;
            } else {
                break;
            }
        }
        if(linePrice == 2)
            return new Pair[]{new Pair(0,j), new Pair(1,j), new Pair(2,j)};
        return new Pair[0];
    }

    private Vector<Pair<Integer, Integer>> checkHorizontal(int i, int j) {
        int linePrice = 0;
        for(int k = j-1; k >= 0; k--){
            if(game.getField()[i][k].getPlayer().getSymbol().equals("")) {
                linePrice++;
            } else {
                break;
            }
        }
        for(int k = j+1; k <= game.getField()[i].length-1; k++){
            if(game.getField()[i][k].getPlayer().getSymbol().equals("")) {
                linePrice++;
            } else {
                break;
            }
        }
        if(linePrice == 2)
            return new Pair[]{new Pair(i,0), new Pair(i,1), new Pair(i,2)};
        return new Pair[0];
    }

    private Vector<Pair<Integer, Integer>> checkDiagonal(int i, int j){
        if((i == 0 && j == 0))
        {
            if(game.getField()[1][1].getPlayer().getSymbol().equals("") && game.getField()[2][2].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(0,0), new Pair(1,1), new Pair(2,2)};
            }
        }
        if((i == 1 && j == 1))
        {
            if(game.getField()[0][0].getPlayer().getSymbol().equals("") && game.getField()[2][2].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(0,0), new Pair(1,1), new Pair(2,2)};
            }
        }
        if((i == 2 && j == 2))
        {
            if(game.getField()[1][1].getPlayer().getSymbol().equals("") && game.getField()[0][0].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(0,0), new Pair(1,1), new Pair(2,2)};
            }
        }
        //Если находимся на вторичной диагонали
        if((i == 2 && j == 0))
        {
            if(game.getField()[1][1].getPlayer().getSymbol().equals("") && game.getField()[0][2].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(2,0), new Pair(1,1), new Pair(0,2)};
            }
        }
        if((i == 1 && j == 1))
        {
            if(game.getField()[2][0].getPlayer().getSymbol().equals("") && game.getField()[0][2].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(2,0), new Pair(1,1), new Pair(0,2)};
            }
        }
        if((i == 0 && j == 2))
        {
            if(game.getField()[1][1].getPlayer().getSymbol().equals("") && game.getField()[2][0].getPlayer().getSymbol().equals("")){
                return new Pair[]{new Pair(2,0), new Pair(1,1), new Pair(0,2)};
            }
        }
        return new Pair[0];
    }
*/
}
