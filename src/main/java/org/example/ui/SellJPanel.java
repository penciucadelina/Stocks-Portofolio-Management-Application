package org.example.ui;

import org.example.services.UserPortfolio;
import org.example.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SellJPanel extends JPanel {
    private StockMarketJFrame mainFrame;

    public SellJPanel(StockMarketJFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 2));

        JPanel sellPanel = new JPanel();
        sellPanel.setLayout(new GridLayout(10, 2));

        JLabel availableFundsLabel = new JLabel("Available funds:");
        JTextField availableFundsTextField = new JTextField(mainFrame.getPortfolio().getCash().toPlainString() + " $");
        availableFundsTextField.setEditable(false);

        JLabel symbolLabel = new JLabel("Symbol:");
        JComboBox<String> symbolComboBox = new JComboBox<>();
        symbolComboBox.setModel(new DefaultComboBoxModel(mainFrame.getMarketService().getSymbols()));

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityTextField = new JTextField();

        JLabel costLabel = new JLabel("Total value:");
        JTextField costTextField = new JTextField();
        costTextField.setEditable(false);

        JButton sellButton = new JButton("Sell");
        sellButton.setEnabled(false);
        sellButton.addActionListener(e -> {
            BigDecimal cost = BigDecimal.valueOf(Double.valueOf(costTextField.getText()));
            String symbol = (String) symbolComboBox.getSelectedItem();
            Integer quantity = Integer.parseInt(quantityTextField.getText());
            UserPortfolio portfolio = mainFrame.getPortfolio();

            if (portfolio.getShares().containsKey(symbol)) {
                Integer remainingShares = portfolio.getShares().get(symbol);
                if (quantity <= remainingShares) {
                    portfolio.setCash(portfolio.getCash().add(cost));
                    portfolio.getShares().compute(symbol, (key, value) -> value != null ? value - quantity : -quantity);

                    availableFundsTextField.setText(Utils.formatBigDecimal(portfolio.getCash()) + " $");
                    quantityTextField.setText("");
                    costTextField.setText("");
                    sellButton.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "You don't have enough actions of " + symbol + " in your portfolio to sell.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "You have already sold all actions of " + symbol + " from your portfolio.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton costButton = new JButton("Get value");
        costButton.addActionListener(e ->
                calculateTotalCostActionPerformed(symbolComboBox, quantityTextField, costTextField, sellButton));

        sellPanel.add(availableFundsLabel);
        sellPanel.add(availableFundsTextField);
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(symbolLabel);
        sellPanel.add(symbolComboBox);
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(quantityLabel);
        sellPanel.add(quantityTextField);
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(costLabel);
        sellPanel.add(costTextField);
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(new JPanel()); // empty cell in the grid
        sellPanel.add(costButton);
        sellPanel.add(sellButton);
        add(sellPanel);
        add(new JPanel()); // empty cell in the grid
        add(new JPanel()); // empty cell in the grid
        add(new JPanel()); // empty cell in the grid
    }

    private void calculateTotalCostActionPerformed(JComboBox<String> symbolComboBox,
                                                   JTextField quantityTextField,
                                                   JTextField totalCostTextField,
                                                   JButton sellButton) {
        try {
            String symbol = (String) symbolComboBox.getSelectedItem();
            BigDecimal stockPrice = mainFrame.getMarketService().getStockPrice(symbol);
            UserPortfolio portfolio = mainFrame.getPortfolio();

            if (portfolio.getShares().containsKey(symbol)) {
                try {
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    totalCostTextField.setText(
                            Utils.formatBigDecimal(stockPrice.multiply(new BigDecimal(quantity))));
                    sellButton.setEnabled(true);
                } catch (NumberFormatException e) {
                    totalCostTextField.setText("Invalid quantity value!");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "The selected symbol doesn't exist in your portfolio!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(StockMarketJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
