package org.example.ui;

import org.example.services.StockItem;
import org.example.services.UserPortfolio;
import org.example.services.YahooWebClient;
import org.example.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PortfolioJPanel extends JPanel {
    private JTextField availableCashTf;
    private JTextField portfolioValueTf;
    private JTable portfolioTable;


    public PortfolioJPanel() {
        setLayout(null);

        JLabel portfolioValueL = new JLabel("Portfolio value: ");
        portfolioValueL.setBounds(10, 10, 150, 20);

        portfolioValueTf = new JTextField();
        portfolioValueTf.setBounds(180, 10, 150, 20);
        portfolioValueTf.setEditable(false);

        Object[] columnNames = {"Name", "Symbol", "Shares", "Price", "Currency", "Total Value"};
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        portfolioTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        scrollPane.setBounds(10, 50, 680, 200);

        JLabel availableCashL = new JLabel("Available funds: ");
        availableCashL.setBounds(10, 270, 150, 20);

        availableCashTf = new JTextField();
        availableCashTf.setBounds(180, 270, 150, 20);
        availableCashTf.setEditable(false);

        add(portfolioValueL);
        add(portfolioValueTf);
        add(availableCashL);
        add(availableCashTf);
        add(scrollPane);
    }

    public void refreshView(UserPortfolio portfolio) {
        if (!portfolio.getShares().isEmpty()) {
            BigDecimal[] portfolioValue = {portfolio.getCash()};
            DefaultTableModel model = (DefaultTableModel) portfolioTable.getModel();
            model.setRowCount(0);

            try {
                Map<String, StockItem> stockItemMap =
                        YahooWebClient.get(portfolio.getShares().keySet().toArray(new String[0])).stream()
                                .collect(Collectors.toMap(
                                        StockItem::getSymbol, // stockItem->stockItem.getSymbol()
                                        Function.identity() // stockItem -> stockItem
                                ));

                portfolio.getShares().forEach((symbol, quantity) -> {
                    BigDecimal totalValue = stockItemMap.get(symbol).getPrice().multiply(BigDecimal.valueOf(quantity));

                    portfolioValue[0] = portfolioValue[0].add(totalValue);

                    model.addRow(new Object[]{
                            stockItemMap.get(symbol).getName(),
                            symbol,
                            quantity.toString(),
                            Utils.formatBigDecimal(stockItemMap.get(symbol).getPrice()),
                            stockItemMap.get(symbol).getCurrency(),
                            Utils.formatBigDecimal(totalValue)
                    });
                });

                portfolioValueTf.setText(Utils.formatBigDecimal(portfolioValue[0]) + " $");
                availableCashTf.setText(Utils.formatBigDecimal(portfolio.getCash()) + " $");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            portfolioValueTf.setText(Utils.formatBigDecimal(portfolio.getCash()) + " $");
            availableCashTf.setText(Utils.formatBigDecimal(portfolio.getCash()) + " $");
        }
    }
}
