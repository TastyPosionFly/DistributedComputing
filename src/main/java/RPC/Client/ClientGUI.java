package RPC.Client;

import RPC.Charge;
import RPC.message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private JTextField cardNumberField;
    private JTextField amountField;
    private JButton sendButton;
    private JComboBox<String> operationComboBox;
    private JTextArea responseArea;


    public ClientGUI() {
        createUI();
    }

    private void createUI() {
        setTitle("客户端");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        operationComboBox = new JComboBox<>(new String[]{"消费", "充值"});
        cardNumberField = new JTextField();
        amountField = new JTextField();
        sendButton = new JButton("发送");
        responseArea = new JTextArea();
        responseArea.setEditable(false);

        pane.add(new JLabel("操作类型:"));
        pane.add(operationComboBox);
        pane.add(new JLabel("卡号:"));
        pane.add(cardNumberField);
        pane.add(new JLabel("金额:"));
        pane.add(amountField);
        pane.add(sendButton);
        pane.add(new JScrollPane(responseArea));

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String operation = (String) operationComboBox.getSelectedItem();
        String cardNumber = cardNumberField.getText().trim();
        String amount = amountField.getText().trim();
        String operationCode = operation.equals("消费") ? "0" : "1";

        String formattedAmount = String.format("%05d", Integer.parseInt(amount));
        String request = operationCode + cardNumber + formattedAmount + "55746";

        Charge charge = (Charge) DynamicProxyFactory.getProxy(Charge.class);
        message result = charge.charge(request);
        System.out.println(result.getResult());

        responseArea.setText("Server response: " + result.getResult());

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ClientGUI clientGUI = new ClientGUI();
            clientGUI.setVisible(true);
        });
    }
}